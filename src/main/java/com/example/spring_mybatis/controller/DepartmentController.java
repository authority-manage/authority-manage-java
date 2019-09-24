package com.example.spring_mybatis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_mybatis.common.ResultDTO;
import com.example.spring_mybatis.controller.base.BaseController;
import com.example.spring_mybatis.dao.ConnectEmpInfoMapper;
import com.example.spring_mybatis.dao.EmpInfoMapper;
import com.example.spring_mybatis.model.Department;
import com.example.spring_mybatis.model.DepartmentInfo;
import com.example.spring_mybatis.model.EmpInfo;

@CrossOrigin
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {
	List<DepartmentInfo> son = new ArrayList<DepartmentInfo>();
	boolean check = true;

	// 部门管理添加
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultDTO add(@RequestBody Department departmentInfo) {
		int res = serviceFacade.getDepartmentService().insert(departmentInfo);
		return success(res);
	}

	// 部门管理查看
	@RequestMapping(value = "/selectByDepartmentId", method = RequestMethod.GET)
	public ResultDTO selectByPrimaryKey(@RequestParam("departmentId") String departmentId) {
		List<Department> departmentInfo = serviceFacade.getDepartmentService().selectByPrimaryKey(departmentId);
		if (departmentInfo == null) {
			return noData();
		}
		return success(departmentInfo);
	}

	// 部门管理修改
	@RequestMapping(value = "/updateByDepartmentId", method = RequestMethod.POST)
	public ResultDTO updateByPrimaryKey(@RequestBody Department departmentId) {

		int res = serviceFacade.getDepartmentService().updateByPrimaryKey(departmentId);
		return success(res);

	}

	@RequestMapping(value = "/checkSon", method = RequestMethod.GET)
	public ResultDTO checkSon(@RequestParam("departmentId") String departmentId, @RequestParam("departmentId1") String departmentId1) {
		check = true;
		son.clear();
		dg(departmentId);
		System.out.println("本身ID:" + departmentId);
		son.forEach(item -> {
			System.out.println(item.getDepartmentName() + "  父ID：" + item.getParentId());
			if (item.getParentId().equals(departmentId1) ) {
				check = false;
			}
			
		});
		if(departmentId1.equals(serviceFacade.getDepartmentInfoService().selectByPrimaryKey(departmentId).get(0).getParentId())) {
			check = false;
		}

		if (check) {
			return success(0);
		} else {
			return checkSon(0);
		}

	}

	public void dg(String departmentId) {
		List<DepartmentInfo> allSon = serviceFacade.getDepartmentInfoService().selectByParentId(departmentId);

		allSon.forEach(item -> {
			son.add(item);
			dg(item.getDepartmentId());
		});
	}

	// 部门管理删除（用修改来完成）
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultDTO deleteByPrimaryKey(@RequestBody Department departmentId) {
		int res = serviceFacade.getDepartmentService().deleteByPrimaryKey(departmentId);
		return success(res);
	}

	// 直接查询全部
	@RequestMapping(value = "/selectAll", method = RequestMethod.GET)
	public ResultDTO selectAll() {
		List<Department> departmentInfo = serviceFacade.getDepartmentService().selectAll();
		return success(departmentInfo);
	}

	// 部门父子级排序
	@RequestMapping(value = "/selectAllForParentIdDepartmentId", method = RequestMethod.GET)
	public ResultDTO selectAllForParentIdDepartmentId() throws Exception {
		List<Department> departmentInfo = serviceFacade.getDepartmentService().selectAllForParentIdDepartmentId();
		return success(departmentInfo);
	}

}
