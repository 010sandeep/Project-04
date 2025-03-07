package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.DoctorBean;
import com.rays.pro4.Bean.ProductBean;
import com.rays.pro4.Model.DoctorModel;
import com.rays.pro4.Model.ProductModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Dob"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "mobileNo"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("expertise"))) {
			request.setAttribute("expertise", PropertyReader.getValue("error.require", "expertise"));
			pass = false;
		}

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		DoctorBean bean = new DoctorBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setMobile(DataUtility.getString(request.getParameter("mobile")));

		bean.setDob(DataUtility.getDate(request.getParameter("dob")));

		bean.setExpertise(DataUtility.getString(request.getParameter("expertise")));

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		DoctorModel model = new DoctorModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println("doctor Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			DoctorBean bean;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		DoctorModel model = new DoctorModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			DoctorBean bean = (DoctorBean) populateBean(request);

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("doctor is successfully Updated", request);
				} catch (Exception e) {
					System.out.println("doctor not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("doctor is successfully Added", request);
					ServletUtility.forward(getView(), request, response);
					bean.setId(pk);
				} catch (Exception e) {
					System.out.println("doctor not added");
					e.printStackTrace();
				}

			}

		}

		if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {

		return ORSView.DOCTOR_VIEW;
	}

}
