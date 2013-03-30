/*
 * Copyright 2013 OW2 Nanoko Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package authenticity.validator;

import javax.validation.ConstraintValidator;

import authenticity.Constants;
import play.api.libs.Crypto;
import play.libs.F;
import play.mvc.Http.Session;

/**
 * This class defined a new Play validator
 * 
 * @author orefalo
 */
public class AuthenticityTokenValidator extends play.data.validation.Constraints.Validator<Object> implements
		ConstraintValidator<AuthenticityToken, Object> {

	/* Default error message */
	final static public String message = "error.browserid";

	/**
	 * Validator initialization.
     * Can be used to initialize the validation based on
	 * parameters passed to the annotation
	 */
	public void initialize(AuthenticityToken constraintAnnotation) {
	}

	/**
	 * The validation itself.
     * Quite simple, we extracts the token from the current session,
	 */
	public boolean isValid(Object uuid) {

		Session session = play.mvc.Http.Context.current().session();
		String authToken = session.get(Constants.AUTH_TOKEN);
		session.remove(Constants.AUTH_TOKEN);
		
		if (authToken == null || uuid == null) {
			return false;
        }

		String sign = Crypto.sign(uuid.toString());
		return authToken.equals(sign);
	}

    /**
     * Let's keep this simple... not supported
     * @return {@literal null}
     */
    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return null;
    }

    /**
	 * Constructs a validator instance.
	 */
	public static play.data.validation.Constraints.Validator<Object> authenticationToken() {
		return new AuthenticityTokenValidator();
	}
}