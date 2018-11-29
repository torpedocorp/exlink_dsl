/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

package kr.co.bizframe.exlink.test.rest;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

/**
 * Setup Jetty security to use basic authentication for all the paths.
 * <p/>
 * In a real use-case you would use more stronger security or integrate to a LDAP or some other use-store.
 * But for this little example we load the users from a file.
 */
public class JettySecurity {

    public static ConstraintSecurityHandler createSecurityHandler() {
        Constraint constraint = new Constraint("BASIC", "customer");
        constraint.setAuthenticate(true);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setPathSpec("/*");

        ConstraintSecurityHandler handler = new ConstraintSecurityHandler();
        handler.addConstraintMapping(mapping);
        handler.setAuthenticator(new BasicAuthenticator());
        handler.setLoginService(new HashLoginService("RiderAutoParts", "src/main/resources/users.properties"));

        return handler;
    }

}
