package bidmart.fyp.pk.bidmartpk;

/**
 * Created by CyberHJ on 12/3/2017.
 */

public class AdInformation {


    public String pname;
    public String pprice;
    public String pdesc;
    public String pcategory;
    public String sname;
    public String productID;
    public String adDate;
    public String imageUri;
    public String sellerNumber;

    public AdInformation(String pname, String pprice, String pdesc, String pcategory, String sname, String productID, String adDate, String imageUri, String sellerNumber) {
        this.pname = pname;
        this.pprice = pprice;
        this.pdesc = pdesc;
        this.pcategory = pcategory;
        this.sname = sname;
        this.productID = productID;
        this.adDate = adDate;
        this.imageUri=imageUri;
        this.sellerNumber = sellerNumber;


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

   public String getImageUri() {
        return imageUri;
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

   public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
   }

    public String getProductID() {
        return productID;
    }

    public String getAdDate() {
        return adDate;
    }

    public AdInformation() {

    }


}
