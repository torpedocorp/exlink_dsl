package kr.co.bizframe.exlink.test.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    @XmlAttribute
    private int id;
    @XmlAttribute
    private String partName;
    @XmlAttribute
    private int amount;
    @XmlAttribute
    private String customerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    
    public String toString(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("id="+id);
    	sb.append("partName="+partName);
    	sb.append("amount="+amount);
    	sb.append("customerName="+customerName);
    	return sb.toString();
    }
}
