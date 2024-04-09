
package Management.journalism.Controller;

import Management.journalism.model.FileEntity;

import Management.journalism.model.NewsEntity;
import Management.journalism.model.NewsUploadForm;
import Management.journalism.repository.FileRepository;
import Management.journalism.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Controller
public class userController {


	
	
	 @Autowired
	    private NewsRepository newsRepository;
	 @Autowired
	    private FileRepository fileRepository;
	 @Autowired
	 	loginController logincontroller;
	 
	 @GetMapping("/logout")
		public String logouyt(Model model)
		{
			return "login";
		}
	 
	 
	@GetMapping("/postnew")
	public String postnews(Model model)
	{
		 model.addAttribute("newsUploadForm", new NewsUploadForm());
		return "uploadPage";
	}
	
	 @PostMapping("/upload")
	    public String uploadNews(@ModelAttribute("newsUploadForm") NewsUploadForm newsUploadForm, Model model) {
	        try {
	        	System.out.println(newsUploadForm);
	            String fileName = System.currentTimeMillis() + "-" + newsUploadForm.getImage().getOriginalFilename();
	            String filePath = Paths.get("src/main/resources/static/images/", fileName).toString();

	            FileEntity fileEntity = new FileEntity();
	            fileEntity.setFileName(fileName);
	            fileEntity.setFileType(newsUploadForm.getImage().getContentType());
	            fileEntity.setFileUrl("/images/" + fileName);
	            fileRepository.save(fileEntity);

	           
	            NewsEntity newsEntity = new NewsEntity();
	            newsEntity.setTitle(newsUploadForm.getTitle());
	            newsEntity.setContent(newsUploadForm.getContent());
	            newsEntity.setFileEntity(fileEntity);
	            newsRepository.save(newsEntity);

	          
	            Files.write(Paths.get(filePath), newsUploadForm.getImage().getBytes());

	           
	        } catch (IOException e) {
	            e.printStackTrace();
	           
	        }
	        return "redirect:/newsFeed";
	    }
	 
	 @GetMapping("/newsFeed")
	 public String newsFeed(Model model)
	 {
		
		 String username=logincontroller.getusername();
		 System.out.println(username);
		 model.addAttribute("userName",username);
		 
		  List<NewsEntity> newsList = newsRepository.findAll();
	        model.addAttribute("newsList", newsList);
		
		 return "newsFeed";
	 }
	 
	 @GetMapping("/journal")
	 public String jofeed(Model model)
	 {
		 String username=logincontroller.getusername();
		 System.out.println(username);
		 model.addAttribute("userName",username);
		 List<NewsEntity> newsList = newsRepository.findAll();
	        model.addAttribute("newsList", newsList);
		 return "journalField";
	 }
	 
	 
		/*
		 * @PostMapping("/upvote/{id}") public String upvote(@PathVariable Long id) {
		 * NewsEntity details=newsRepository.findById(id).orElseThrow(() -> new
		 * IllegalArgumentException("Invalid news Id:" + id));
		 * details.setUpvote(details.getUpvote()+1); newsRepository.save(details);
		 * return "redirect:/journal"; }
		 * 
		 * @PostMapping("/downvote/{id}") public String downvote(@PathVariable Long id)
		 * { NewsEntity details=newsRepository.findById(id).orElseThrow(() -> new
		 * IllegalArgumentException("Invalid news Id:" + id));
		 * 
		 * details.setDownvote(details.getDownvote()+1); newsRepository.save(details);
		 * return "redirect:/journal"; }
		 */
	 private Set<Long> upVotedNews = new HashSet<>();
	    private Set<Long> downVotedNews = new HashSet<>();

	    
	    @PostMapping("/vote/{type}/{id}")
	    public @ResponseBody Map<String, Integer> vote(@PathVariable String type, @PathVariable Long id) {
	        NewsEntity details = newsRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid news Id:" + id));

	        if ("up".equalsIgnoreCase(type)) {
	            if (!upVotedNews.contains(id)) {
	                details.setUpvote(details.getUpvote() + 1);
	                upVotedNews.add(id);
	                if (downVotedNews.contains(id)) {
	                    details.setDownvote(details.getDownvote() - 1);
	                    downVotedNews.remove(id);
	                }
	            }
	        } else if ("down".equalsIgnoreCase(type)) {
	            if (!downVotedNews.contains(id)) {
	                details.setDownvote(details.getDownvote() + 1);
	                downVotedNews.add(id);
	                if (upVotedNews.contains(id)) {
	                    details.setUpvote(details.getUpvote() - 1);
	                    upVotedNews.remove(id);
	                }
	            }
	        }

	        newsRepository.save(details);

	        Map<String, Integer> voteCounts = new HashMap<>();
	        voteCounts.put("upvote", details.getUpvote());
	        voteCounts.put("downvote", details.getDownvote());

	        return voteCounts;
	    }


	   
	    
}
