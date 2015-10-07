package boss.boss_rs;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import boss.data.entities.BossAccount;
import boss.data.repositories.BossUserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Path("/{apiVersion}/externalLogin")
public class UserService {

	@PersistenceUnit(unitName = "DS_MASTER")
	protected EntityManagerFactory	emf;
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private BossUserRepository userRepo;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTree(@PathParam("apiVersion") String apiVersion, @DefaultValue("true") @QueryParam("cache") boolean cache) throws JsonProcessingException {
		StopWatch watch = new StopWatch();
		watch.start();


		if (log.isDebugEnabled()) {
			log.trace("Time to execute getTree Service : {}ms", watch.getTime());
		}
		return Response.status(Response.Status.OK).build();
	}
	
	
	@GET
	@Path("/user/{userName}/account")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountDetails(@PathParam("apiVersion") String apiVersion,@PathParam("userName") String userName, @DefaultValue("true") @QueryParam("cache") boolean cache) throws JsonProcessingException {
		StopWatch watch = new StopWatch();
		watch.start();

		EntityManager em = emf.createEntityManager();
		if (log.isDebugEnabled()) {
			log.trace("Time to execute getTree Service : {}ms", watch.getTime());
		}
//		if (!accountValidator.validateAccount(accountId)) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(accountValidator.getResponse(accountId)).build();
//		}
		int userId=2;
		BossUserRepository userRepo = new BossUserRepository();
//BossAccount accountDetails =  userRepo.getUserAccountDetails(em, userId);

		watch.stop();
		if (log.isDebugEnabled()) {
			log.trace("Time to execute ACCOUNTDETAILS for a USER : {}ms", watch.getTime());
		}
		return Response.status(Response.Status.OK).build();

	}

	
	@GET
	@Transactional
	@Path("/user/{userName}/accountBalance")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountBalance(@PathParam("apiVersion") String apiVersion,@PathParam("userName") String userName, @DefaultValue("true") @QueryParam("cache") boolean cache) throws JsonProcessingException {
		StopWatch watch = new StopWatch();
		watch.start();

		EntityManager em = emf.createEntityManager();
		if (log.isDebugEnabled()) {
			log.trace("Time to execute getTree Service : {}ms", watch.getTime());
		}
//		if (!accountValidator.validateAccount(accountId)) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(accountValidator.getResponse(accountId)).build();
//		}
		long accNo=746353;
		//BossUserRepository userRepo = new BossUserRepository();
double accountBalance =  userRepo.account(accNo);

		watch.stop();
		if (log.isDebugEnabled()) {
			log.trace("Time to execute ACCOUNTDETAILS for a USER : {}ms", watch.getTime());
		}
		return Response.status(Response.Status.OK).entity(accountBalance).build();

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/login/**")
	protected ModelAndView handleFormPostAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestURI = request.getRequestURI();
		if (StringUtils.isNotEmpty(requestURI)) {
			if (requestURI.matches(".*[0-9A-Fa-f]{64}$")) {
				log.trace("form post {}", requestURI);
				return new ModelAndView("forward:/api/" + request.getRequestURI());
			}
		}		
		return new ModelAndView("forward:/api/" + request.getRequestURI());
		
	}
		
}