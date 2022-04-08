package model;

/**Inherits Part class*/
public class OutSourced extends Part{
    private String companyName;

    /**OutSourced Constructor*/
    public OutSourced(int partId, String partName, int stock, double price, int min, int max, String companyName) {

        super(partId, partName, stock, price, min, max);
        this.companyName = companyName;
    }
    /**OutSourced CompanyName setter*/
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    /**OutSourced CompanyName getter*/
    public String getCompanyName(){
        return companyName;
    }

}
