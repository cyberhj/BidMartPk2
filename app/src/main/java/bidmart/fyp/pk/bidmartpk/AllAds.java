package bidmart.fyp.pk.bidmartpk;

/**
 * Created by CyberHJ on 12/10/2017.
 */

public class AllAds {
    public String pname;
    public String pprice;
    public String pdesc;
    public String pcategory;
    public String sname;
    public String productID;
    public String adDate;

    public AllAds(String pname, String pprice, String pdesc, String pcategory, String sname, String productID, String adDate) {
        this.pname = pname;
        this.pprice = pprice;
        this.pdesc = pdesc;
        this.pcategory = pcategory;
        this.sname = sname;
        this.productID = productID;
        this.adDate = adDate;
    }

    public String getPname() {
        return pname;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public void setPcategory(String pcategory) {
        this.pcategory = pcategory;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setAdDate(String adDate) {
        this.adDate = adDate;
    }

    public String getPdesc() {
        return pdesc;

    }

    public String getPcategory() {
        return pcategory;
    }

    public String getSname() {
        return sname;
    }

    public String getProductID() {
        return productID;
    }

    public String getAdDate() {
        return adDate;
    }

}