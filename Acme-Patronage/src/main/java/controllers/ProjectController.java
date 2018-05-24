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
import services.CategoryService;
import services.PatronageService;
import services.ProjectService;
import services.UserService;
import domain.Actor;
import domain.Category;
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
	@Autowired
	private CategoryService		categoryService;


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

	//List ordered

	@RequestMapping(value = "/listOrdered", method = RequestMethod.GET)
	public ModelAndView listOrdered(@RequestParam(required = false, defaultValue = "") final String word, @RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Project> projects = new ArrayList<Project>();
		if (word == null || word.equals(""))
			projects = this.projectService.findProjectFutureDueDateOrdered();
		else
			projects = this.projectService.findProjectByKeyWordOrdered(word);

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

	@RequestMapping(value = "/listByCategory", method = RequestMethod.GET)
	public ModelAndView listByCategory(@RequestParam(required = false, defaultValue = "") final String word, @RequestParam final int categoryId, @RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Project> projects = new ArrayList<Project>();
		final Category c = this.categoryService.findOne(categoryId);

		if (word == null || word.equals(""))
			projects = this.projectService.findProjectByCategory(c.getId());
		else
			projects = this.projectService.findProjectByKeyWordCategory(word, c.getId());

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
		result.addObject("projects", projects);
		result.addObject("principal", principal);
		result.addObject("totalAmounts", totalAmounts);
		result.addObject("c", c);
		result.addObject("categoryId", categoryId);
		result.addObject("message", message);
		result.addObject("requestURI", "project/list.do");

		return result;
	}

}
