package View;

import Model.*;
import View.MainUI;
import sun.applet.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    private ArrayList<Client> listClients = new ArrayList<>();
    private ArrayList<Product> listProducts = new ArrayList<>();
    public MainUI userInterface = new MainUI(this);

    public void addCustomer(String nameCustomer, String surnameCustomer){
        Customer c = new Customer(nameCustomer,surnameCustomer);
        listClients.add(c);
    }

    public void addProduct(String nameProduct, String quantityProduct, String priceProduct){
        Product p = new Product(nameProduct, Double.parseDouble(quantityProduct), Double.parseDouble(priceProduct));
        listProducts.add(p);
        userInterface.productEntry(p);
    }

    public void addCompany(String nameCompany){

        Company c = new Company(nameCompany);
        listClients.add(c);
    }

    public void refreshClientList(){
        userInterface.refreshCL();
    }

    public ArrayList<Client> getListCompanies(){
        return listClients;
    }

    public ArrayList<Product> getListProducts(){return listProducts;}

    public void importProductList(){
        ArrayList<Product> arr = null;
        try {
            FileInputStream fileIn = new FileInputStream("exportProducts.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            arr = (ArrayList<Product>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i) {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            System.out.println("Product class not found");
            c.printStackTrace();
        }
        for(int i = 0; i < arr.size(); i++){
            listProducts.add(arr.get(i));
            userInterface.productEntry(arr.get(i));
        }
        //refreshProductList();
    }

    public void exportProductList(){
        try {
            FileOutputStream fileOut = new FileOutputStream("exportProducts.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(listProducts);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in exportProducts.txt");
        }catch(IOException i) {

        }

    }

    public void exportClientList(){
        try {
            FileOutputStream fileOut = new FileOutputStream("exportClients.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(listClients);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in exportClients.txt");
        }catch(IOException i) {

        }

    }

    public void importClientsList(){
        ArrayList<Client> arr = null;
        try {
            FileInputStream fileIn = new FileInputStream("exportClients.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            arr = (ArrayList<Client>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i) {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            System.out.println("Client class not found");
            c.printStackTrace();
        }
        int indexing = 0;
        for(int i = 0; i < arr.size(); i++){
            listClients.add(arr.get(i));
            if(arr.get(i).getId() > indexing)
                indexing = arr.get(i).getId();
        }
        Client.nextId = new AtomicInteger(indexing);
        refreshClientList();
    }

    public Product queryProduct(String nameProduct){
        System.out.print(nameProduct);
        for(int i = 0; i < listProducts.size(); i++) {
            System.out.print(listProducts.get(i).getName() + " ");
        }

        Product gasit = null;
        for(int i = 0; i < listProducts.size(); i++){
            if(listProducts.get(i).getName().equals(nameProduct)){
                System.out.print("true");
                gasit = listProducts.get(i);
            }
        }
        System.out.print(gasit);
        return gasit;
    }

    public double enoughProduct(String nameProduct, String quantityProduct){
        for(int i = 0; i < listProducts.size(); i++){
            if(listProducts.get(i).getName().equals(nameProduct)){
                if(listProducts.get(i).getQuantity() < Double.parseDouble(quantityProduct)){
                    return listProducts.get(i).getQuantity();
                }
            }
        }
        return -1.0;
    }

    public Product getProduct(int index){
        return listProducts.get(index);
    }

    public Product getProduct(String nameProduct){
        for(int i = 0; i < listProducts.size(); i++) {
            if(listProducts.get(i).getName().equals(nameProduct)){
                return listProducts.get(i);
            }
        }
        return null;
    }

    public void removeProduct(Product p){
        listProducts.remove(p);
        userInterface.refreshPL();
    }

    public void removeProduct(int row){
        userInterface.dtm.getValueAt(row,1);
        for(int i = 0; i < listProducts.size(); i++){
            if(listProducts.get(i).getName().equals(userInterface.dtm.getValueAt(row,1))){
                System.out.print(listProducts.get(i).getName());
                removeProduct(listProducts.get(i));
                break;
            }
        }
    }

    public void updateStocks(Factura factura){
        for(int i = 0; i < factura.getNumberProducts(); i++){
            getProduct(factura.getListaProduse().get(i).getName()).setQuantity(getProduct(factura.getListaProduse().get(i).getName()).getQuantity() - factura.getListaProduse().get(i).getQuantity());
        }
        userInterface.refreshPL();
        for(int i = 0; i < listProducts.size(); i++){
            if(listProducts.get(i).getName().equals("Marker")){
                System.out.println(listProducts.get(i).getQuantity());
            }
        }
    }

    public void editProduct(Product product, String newName, String newQuantity, String newPrice){
        product.setName(newName);
        product.setPrice(Double.parseDouble(newPrice));
        product.setQuantity(Double.parseDouble(newQuantity));
        userInterface.refreshPL();
    }

}
