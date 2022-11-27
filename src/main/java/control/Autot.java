//backend
package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

	//Tietojen hakemista varten
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

	//Tietojen lisääminen
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPost()");
		//Luetaan JSON-tiedot POST-pyynnön bodysta ja luodaan niiden perusteella uusi auto
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		Auto auto = new Gson().fromJson(strJSONInput, Auto.class);	//model Auto
		//System.out.println(auto);
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.addItem(auto)) {
			out.println("{\"response\":1}");  //Auton lisääminen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Auton lisääminen epäonnistui {"response":0}
		}
		
	}

	//Tietojen muuttaminen
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");	
	}

	//Tiedojen poistaminen
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doDelete()");
		int id = Integer.parseInt(request.getParameter("id")); //Kutsujen mukana kulkevat arvot ovat AINA merkkejä		
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.removeItem(id)) {
			out.println("{\"response\":1}");  //Auton poistaminen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Auton poistaminen epäonnistui {"response":0}
		}
	}

}
