package boss.boss_rs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boss.data.repositories.BossUserRepository;

@Service
public class BossUserDetailsService implements UserDetailsService {

	@Autowired
	private BossUserRepository userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		   List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
		   String password="";
		   if (username.equals(null)||username.equals("")){
			   return null;
		   }
		   if (userRepo.userExists(username)){
			   
		   
		   int loginAttempts=userRepo.getLoginAttempts(username);
		   
		   if(loginAttempts>=3){
			   String userDetails =
						 (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				SecurityContext sc=SecurityContextHolder.getContext();  
				
			   
			   return new User(username,"",null);
		   }
		   String otpFlag=userRepo.getOTPFlag(username);
		   if(otpFlag=="Y"||otpFlag.equals("Y")){
			   return new User(username,"",true,false,true,true,null);
		   }
		   
		   long userId = userRepo.findIdByName(username);
		   
		   //String password= userRepo.getPasswordByName(username);
		    password = userRepo.getPasswordById(userId);
		   BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(password); 
           List<String> roleList = new ArrayList<String>();
           roleList=userRepo.getRolesbyUserId(userId);
           for (String roles:roleList){
	        //authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        	   authList.add(new SimpleGrantedAuthority(roles));
           }
		   }
           if(authList.isEmpty()){
        	   return null;
           }
		//User user = new User (username,"rishabh",authList);
           User user = new User (username,password,authList);
		return user;
	}
	
	
	
	
	
}
