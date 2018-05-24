/*
 * AboutUsController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PatronageService;
import services.ProjectService;
import services.UserService;
import domain.Actor;
import domain.Project;
import domain.User;

@Controller
@RequestMapping("/project")
public class ProjectController extends AbstractController {

	@Autowired
	private ProjectService		projectService;

	@Autowired
	private PatronageService	patronageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public ProjectController() {
		super();
	}

	//Listing 

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "") final String word, @RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Project> projects = new ArrayList<Project>();
		if (word == null || word.equals(""))
			projects = this.projectService.findProjectFutureDueDate();
		else
			projects = this.projectService.findProjectByKeyWord(word);

		User principal = null;
		if (this.actorService.checkLogin()) {
			final Actor a = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(a, "USER"))
				principal = this.userService.findByPrincipal();
		}
		final Collection<Double> totalAmounts = new ArrayList<Double>();
		for (final Project p : projects)
			totalAmounts.add(this.patronageService.findTotalAmount(p.getId()));
		result = new ModelAndView("project/list");
		result.addObject("totalAmounts", totalAmounts);
		result.addObject("principal", principal);
		result.addObject("projects", projects);
		result.addObject("message", message);
		result.addObject("requestURI", "project/list.do");

		return result;
	}

}
