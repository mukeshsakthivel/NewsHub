package Management.journalism.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import Management.journalism.Service.Serviceprovider;
import Management.journalism.model.Login;
import Management.journalism.model.Signup;
import Management.journalism.repository.Signuprepository;
import Management.journalism.repository.loginrepository;
import jakarta.validation.Valid;

@Controller
public class loginController {

	public  String username;
	
	@Autowired
	loginrepository loginrepo;
	@Autowired
	Signuprepository signuprepo;
	@Autowired
	Serviceprovider service;
	
	  @GetMapping("/")
	    public String showSignupForm(Model model) 
	  {
	        model.addAttribute("signup", new Signup());
	        return "index";
	    }
	
	  @PostMapping("/dashboard")
	  public String signup(@ModelAttribute("signup") @Valid Signup signup, BindingResult bindingResult, Model model) 
	  {
	      if (bindingResult.hasErrors()) {
	          return "index";
	      }

	      // Check for existing name and email
	      if (signuprepo.existsByName(signup.getName())) {
	          bindingResult.rejectValue("name", "error.signup", "Username already exists!");
	          return "index";
	      }

	      if (signuprepo.existsByEmail(signup.getEmail())) {
	          bindingResult.rejectValue("email", "error.signup", "Email already exists!");
	          return "index";
	      }
	      //model.addAttribute("userName", signup.getName());
	      username=signup.getName();
	      service.saveSignup(signup);
	      if ("Common User".equals(signup.getRole()))
	    	  	return "redirect:/newsFeed";
	      return "redirect:/journal";
	  }
	  @GetMapping("/login")
		public String login(Model model)
		{
		  model.addAttribute("login", new Login());
			return "login";
		}
	  
	  @PostMapping("/verification")
	  public String verifyLogin(@ModelAttribute("login") @Valid Login login, BindingResult bindingResult, Model model) 
	  {
		 
	      if (bindingResult.hasErrors()) {
	    	  return "redirect:/login";
	      }

	      
	      Signup user = signuprepo.findByName(login.getName());

	      if (user == null) {
	          bindingResult.rejectValue("name", "error.login", "Invalid username!");
	          return "login";
	      }

	      if (!user.getPassword().equals(login.getPassword())) 
	      {
	          bindingResult.rejectValue("password", "error.login", "Invalid password!");
	          return "login";
	          }

	      if ("Common User".equals(user.getRole()))
	      {
	    	  username=login.getName();
	          
	          return "redirect:/newsFeed";
	      } 
	      else if ("Journalist".equals(user.getRole())) 
	      {
	    	  username=login.getName();
	          model.addAttribute("userName", username);
	          return "redirect:/journal";  
	      } 
	      else {
	          //bindingResult.rejectValue("name", "error.login", "Invalid role!");
	          return "redirect:/login";
	      }
	  }
	  
	  
	  public String getusername()
	  {
		  return username;
	  }
}



	

