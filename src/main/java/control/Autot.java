package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Auto;
import model.dao.Dao;


@WebServlet("/autot/*") //Muuta alkukirjain pieneksi ja tähti perään --> /Autot ----> /autot/*
public class Autot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Autot() {
        System.out.println("Autot.Autot()");  
        //systr ja CTRL + välilyönti
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doGet()");
		String hakusana = request.getParameter("hakusana"); //otetaan hakusana vastaaan
		//System.out.println(hakusana);//tulostetaan konsoliin, tarkistus
		Dao dao = new Dao();
		ArrayList<Auto> autot;
		String strJSON="";
		
		if(hakusana!=null) {//Jos hakusana on olemassa 
			if(!hakusana.equals("")) { //Jos hakusana ei ole tyhjä
				autot = dao.getAllitems(hakusana); //haetaan kaikki hakusanan mukaiset autot
			} else {
				autot = dao.getAllItems(); //haetaan kaikki autot
			}
			strJSON = new Gson().toJson(autot);
		}
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPost()");
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");	
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doDelete()");
	}

}
