package com.biubiu.service;

import com.alibaba.fastjson.JSONObject;
import com.biubiu.core.common.Const;
import com.biubiu.dao.EcsDao;
import com.biubiu.dto.Node;
import com.biubiu.model.Remote;
import com.biubiu.pojo.Ecs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EcsService {

    @Resource
    private EcsDao ecsDao;

    public List<Node> getTree() {
        List<Ecs> list = ecsDao.list();
        return recursion(list, Const.ROOT);
    }

    private List<Node> recursion(List<Ecs> list, Long targetId) {
        List<Node> target = new ArrayList<>();
        List<Ecs> filters = list.stream().filter(v -> v.getParentId().equals(targetId)).collect(Collectors.toList());
        for (Ecs ecs : filters) {
            Node node = new Node();
            Long id = ecs.getId();
            node.setId(id);
            node.setName(ecs.getName());
            String type = ecs.getType();
            node.setType(type);
            if (Const.FOLDER.equalsIgnoreCase(type)) {
                node.setIcon(Const.FOLDER_ICON);
                // 追加孩子节点
                List<Node> children = recursion(list, id);
                node.setChildren(children);
            } else {
                node.setIcon(Const.LEAF_ICON);
                String config = ecs.getConfig();
                Remote remote = JSONObject.parseObject(config, Remote.class);
                node.setConfig(remote);
            }
            target.add(node);
        }
        return target;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insert(Ecs ecs) {
        return ecsDao.insert(ecs);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int update(Ecs ecs) {
        return ecsDao.update(ecs);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int delete(Long id) {
        List<Ecs> list = ecsDao.listById(id);
        Set<Long> toList = new HashSet<>();
        Set<Long> toDel = new HashSet<>();
        for(Ecs ecs: list) {
            // 如论是文件夹还是节点都要加入到待删除队列中
            toDel.add(ecs.getId());
            if(Const.FOLDER.equalsIgnoreCase(ecs.getType())) {
                toList.add(ecs.getId());
            }
        }
        recursion(toList, toDel);
        // 执行批量删除
        if(toDel.size() == 0) {
            return 0;
        }
        return ecsDao.batchDelete(toDel);
    }

    private void recursion(Set<Long> toList, Set<Long> toDel) {
        if(toList.size() == 0) {
            return;
        }
        Set<Long> ids = new HashSet<>();
        List<Ecs> list = ecsDao.listByParentIds(toList);
        for(Ecs ecs: list) {
            // 如论是文件夹还是节点都要加入到待删除队列中
            toDel.add(ecs.getId());
            if(Const.FOLDER.equalsIgnoreCase(ecs.getType())) {
                ids.add(ecs.getId());
            }
        }
        recursion(ids, toDel);
    }

}
