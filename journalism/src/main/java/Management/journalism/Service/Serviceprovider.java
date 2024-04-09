package Management.journalism.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Management.journalism.model.Signup;
import Management.journalism.repository.Signuprepository;

@Service
public class Serviceprovider {
	
	@Autowired
	Signuprepository signuprepo;
	public Signup saveSignup(Signup sign)
	{
		return signuprepo.save(sign);
	}

}
