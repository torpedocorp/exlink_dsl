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

package kr.co.bizframe.exlink.test;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
 
public class DemoExecutor
{
    public static void main(String[] args)
    {
    	
    	DemoExecutor demo = new DemoExecutor();
        Integer threadCounter = 0;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(200);
 
        ThreadPoolExecutor  executor = new ThreadPoolExecutor (10,
                                            100, 60000, TimeUnit.MILLISECONDS, blockingQueue);
 
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r,
                    ThreadPoolExecutor executor) {
                System.out.println("DemoTask Rejected : " + ((DemoTask) r).getName());
                System.out.println("Waiting for a second !!");
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Lets add another time : " + ((DemoTask) r).getName());
                executor.execute(r);
            }
        });
        // Let start all core threads initially
        executor.prestartAllCoreThreads();
        while (true) {
            threadCounter++;
            // Adding threads one by one
            System.out.println("Adding DemoTask : " + threadCounter);
            executor.execute(demo.new DemoTask(threadCounter.toString()));
            int activeCount = ((ThreadPoolExecutor)executor).getActiveCount();
    		int queuesize = ((ThreadPoolExecutor)executor).getQueue().size();
    		 System.out.println("##### wait decrease active therad count ="+activeCount+ " queue thread size="+queuesize);
            if (threadCounter == 2000)
                break;
        }
    }
    
    public class DemoTask implements Runnable
    {
        private String name = null;
     
        public DemoTask(String name) {
            this.name = name;
        }
     
        public String getName() {
            return this.name;
        }
     
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Executing : " + name);
        }
    }
 
}