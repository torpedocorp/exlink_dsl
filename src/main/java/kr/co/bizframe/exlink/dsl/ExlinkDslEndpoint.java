package kr.co.bizframe.exlink.dsl;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author sushin
 *
 */
@UriEndpoint(firstVersion = "1.4.0", scheme = "exlink", title = "exlink", syntax = "exlink:recv|send", consumerClass = ExlinkDslConsumerDefault.class, label = "exlink:recv|send")
public class ExlinkDslEndpoint extends DefaultEndpoint {
	
	public static Logger logger = LoggerFactory.getLogger(ExlinkDslEndpoint.class);

	@UriParam(label = "fileFormat", description = "fileFormat")
	private String fileFormat;
	
	private String workType;
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(" fileFormat="+this.fileFormat);
		sb.append(" workType="+this.workType);
		return sb.toString();
	}

	public ExlinkDslEndpoint(String uri, ExlinkDslComponent component) {
		super(uri, component);
	}

	public ExlinkDslEndpoint(String endpointUri) {
		super(endpointUri);
	}

	public Producer createProducer() throws Exception {
		return new ExlinkDslProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ endpoint createConsumer" );
		return new ExlinkDslConsumerPoll(this, processor);
	}

	public boolean isSingleton() {
		return true;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
}