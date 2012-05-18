package com.htong.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.log4testng.Logger;

import com.htong.dao.IndexNodeModelDao;
import com.htong.dao.WellModelDao;
import com.htong.domain.IndexNodeModel;
import com.htong.domain.WellModel;
import com.htong.util.WellUtil;

@Service
public class WellService {
	private static final Logger log = Logger.getLogger(WellService.class);
	@Autowired
	private IndexNodeModelDao indexNodeModelDao;
	@Autowired
	private WellModelDao wellModelDao;
	
	public Map<String, Object> getWellTreeInfo() {
		IndexNodeModel indexNodeModel = indexNodeModelDao.getIndexNodeModel();
		if(indexNodeModel != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", indexNodeModel.getName());
			map.put("iconCls", "icon-save");
			List<Map<String, Object>> childrenList = new ArrayList<Map<String,Object>>();
			map.put("children", childrenList);
			
			handleWell(indexNodeModel, childrenList);
			
			return map;
		}
		return null;
		
		
	}
	
	private void handleWell(IndexNodeModel indexNodeModel, List<Map<String, Object>> childrenList) {
		List<IndexNodeModel> indexNodeList = indexNodeModel.getIndexNodes();
		if(indexNodeList!=null && !indexNodeList.isEmpty()) {
			for(IndexNodeModel indexNode : indexNodeList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("text", indexNode.getName());
				map.put("iconCls", "icon-save");
				List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
				map.put("children", children);
				
				childrenList.add(map);
				
				indexNode.setParentObject(indexNodeModel);
				handleWell(indexNode, children);
			}
		} else if(indexNodeList == null) {//叶子
			String path = WellUtil.getPath(indexNodeModel);
			log.debug(path);
			List<WellModel> wellModelList = wellModelDao.getWellsByPath(path);
			
			for(WellModel well : wellModelList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("text", well.getName());
				//map.put("iconCls", "icon-save");
				Map<String, Object> attributes = new HashMap<String,Object>();
				attributes.put("iswell", true);
				map.put("attributes", attributes);
				
				childrenList.add(map);
			}

		}
	}

}
