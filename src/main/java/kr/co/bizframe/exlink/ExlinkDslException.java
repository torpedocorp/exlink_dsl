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

package kr.co.bizframe.exlink;

public class ExlinkDslException extends Exception {

	protected int _code;

	public static final int READABLE_EXCEPTION = 9900;

	public ExlinkDslException() {
		super();
	}

	public ExlinkDslException(String msg) {
		super(msg);
	}

	public ExlinkDslException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ExlinkDslException(Throwable cause) {
		super(cause);
	}

	public ExlinkDslException(int code) {
		super();
		_code = code;
	}

	public ExlinkDslException(int code, String msg) {
		super(msg);
		_code = code;
	}
	public ExlinkDslException(int code, String msg, Throwable cause) {
		super(msg, cause);
		_code = code;
	}

	public int getCode() {
		return _code;
	}
}
