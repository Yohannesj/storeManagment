
package departmental.store.managment.system;

import static departmental.store.managment.system.GUI.DEFAULT_UI;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * store class is the class responsible 
 *          to manage packages(i.e registering a package)
 *          to store packages
 *          to generate a store object 
 */
public class Store implements Serializable, displayable{
    private int top;
    private final int size=100;
    private Package[] list;
    private String name;
    public static final String[] FIELDS = {"code","name","discription","amount","price"};
    public Store() {
        top=0;
        this.list = new Package[size];
    }
    /**
     * initialize the store object
     * @param name name of the store
     **/
    public Store(String name){
        this();
        this.name=name;
    }

    /**
     * this method is used to display the list of packages that the store has.
     * it is inherited from displayable interface
     */
    @Override
    public void display(){
        if(DEFAULT_UI!=userInterface.TERMINAL){
            gShow();
        }else{
            tShow();
        }
    }
    /**
     * this function return true if it successfully adds the package i.e. the package is unique.
     * else it return false if the package already exits with the same code
     *
     * @param p the package to register.
     * @return true success full addition of package. false on filer
     */
    public boolean append(Package p){
        if(!isInTheStore(p.getCode())){
            list[top++]=p;
            return true;
        }
        return false;
    }
    /**
     * register a package with proper UI
     */
   public void enterPackage(){
        if(DEFAULT_UI!=userInterface.TERMINAL){
            gEnter();
        }else{
            tEnter();
        }
    }
    /**
     * graphically register a package
     */
    public void gEnter(){
        Package newpackage= new Package();
        int i=0;
        String displayablemsg="insert package infomation \n";
        try {
            String temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
            newpackage.setCode(temp);
            if(isInTheStore(temp)){
            //package is found
               newpackage = packageWith(temp);

               String packageInfo = "code: "+temp+"\n"
                         + "name: " +newpackage.getName()+"\n"
                         + "discription: "+newpackage.getDiscription()+"\n"
                         + "price: "+newpackage.getPrice()+"\n"
                         + "amount: "+newpackage.getAmount()+"\n"
                         + "dispatched: "+newpackage.getDispatched()+"\n"
                         + "issued: "+newpackage.getIssued();
                 int amount = Integer.parseInt(JOptionPane.showInputDialog(null,"package found! \n"+packageInfo+"\n insert the amount of package to insert."));
                 if(amount>0){
                    newpackage.addAmount(amount);
                    JOptionPane.showMessageDialog(null,"the package has been sucessfully registered");
                 }else{
                     throw new NumberFormatException("invalid amount.");
                 }

            }else{
                displayablemsg+=FIELDS[i]+": "+temp+"\n";
                i++;
                temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                newpackage.setName(temp);
                displayablemsg+=FIELDS[i]+": "+temp+"\n";
                i++;
                temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                newpackage.setDiscription(temp);
                displayablemsg+=FIELDS[i]+": "+temp+"\n";
                i++;
                temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                newpackage.setAmount(Long.parseLong(temp));
                displayablemsg+=FIELDS[i]+": "+temp+"\n";
                i++;
                temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                newpackage.setPrice(Double.parseDouble(temp));
                displayablemsg+=FIELDS[i]+": "+temp+"\n";
                append(newpackage);
            }
         } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(null,e.getLocalizedMessage()+" please try again","error alert message",JOptionPane.ERROR_MESSAGE);
             gEnter();
        }
    }
     /**
     * register a package in the terminal(console)
     */
    public void tEnter(){
        Package newpackage= new Package();
        Scanner in = new Scanner(System.in);
        int i=0;
        String displayablemsg="insert package infomation \n";
        System.out.print(displayablemsg +FIELDS[i]+"=");
        String temp = in.next();
        newpackage.setCode(temp);
        if(isInTheStore(temp)){
        //package is found
           newpackage = packageWith(temp);

           String packageInfo = "code: "+temp+"\n"
                     + "name: " +newpackage.getName()+"\n"
                     + "discription: "+newpackage.getDiscription()+"\n"
                     + "price: "+newpackage.getPrice()+"\n"
                     + "amount: "+newpackage.getAmount()+"\n"
                     + "dispatched: "+newpackage.getDispatched()+"\n"
                     + "issued: "+newpackage.getIssued();

             System.out.println("package found! \n"+packageInfo+"\n insert the amount of package to insert.");
             int amount = in.nextInt();
             if(amount>0){
                newpackage.addAmount(amount);
                System.out.println("the package has been sucessfully registered");
             }else{
                 System.out.println("invaild amount.");
             }
        }else{
            i++;
            System.out.print(displayablemsg +FIELDS[i]+"=");
            newpackage.setName(in.next());
            i++;
            System.out.print(displayablemsg +FIELDS[i]+"=");
            newpackage.setDiscription(in.next());
            i++;
            System.out.print(displayablemsg +FIELDS[i]+"=");
            newpackage.setAmount(in.nextLong());
            i++;
            System.out.print(displayablemsg +FIELDS[i]+"=");
            newpackage.setPrice(in.nextDouble());
            append(newpackage);
        }
    }
    //display the store in the console
    @Override
    public void tShow(){
        System.out.println(toString());
    }
    //display the store grahically
    @Override
    public void gShow(){
        if(top!=0){
            String[] columen = {"code", "name ", "discription" , "price" , "amount" , "dispatched" , "issued"};
            int row = top+1;
            Object[][] table = new Object[row][7];
            table[0]=columen;
            for(int i=0;i<top;i++){
                table[i+1]=list[i].toArray();
            }
            JTable t = new JTable(table,columen);
            JOptionPane.showMessageDialog(null,t,"summery of the store",JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null,toString(),"summery of the store",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // convert the stote into the text formate
    @Override
    public String toString(){
        String summeryStr="";
        if(top==0){
              summeryStr +="the store is empty\n";
        }else{
            summeryStr+=String.format("%-4s | %-5s | %-10s | %-9s | %-4s | %-4s | %-4s%n","code", "name ", "discription" , "price" , "amount" , "dispatched", "issued");
            summeryStr+="-------------------------------------------------------------\r\n";
            for(int i=0;i<top;i++)
               summeryStr+=list[i];
            summeryStr+="-------------------------------------------------------------";
        }
        return summeryStr;
    }
    /**
     * checks if the package is in the store.
     * @param code the code of a package
     * @return true if the package with code exists in the store else return false
     */
    public boolean isInTheStore(String code){
        return (null!=packageWith(code));
    }

    /**
     * sort the store packages by their code
     */
    public void sort(){
        Package[] temp= new Package[top];
        System.arraycopy(list,0,temp,0,top);
        Arrays.sort(temp);
        System.arraycopy(temp,0,list,0,top);
        if(GUI.DEFAULT_UI==userInterface.GRAPHICAL){
           JOptionPane.showMessageDialog(null, "sucessfully sorted packages by thier code.");
        }else{
            System.out.println("sucessfully sorted packages by thier code.");
        }
    }
    /**
     * a method for setting the UI.
     */
    public void setting(){
        if(GUI.DEFAULT_UI==userInterface.GRAPHICAL){
            gSetting();
        }else{
            tSetting();
        }
    }
    // toggle the UI with grahpical UI
    public void gSetting(){
        int b;
        b = JOptionPane.showConfirmDialog(null, "do you want to change the user interface.","change the UI",JOptionPane.YES_NO_OPTION);
        if(b==0)
            GUI.toggle();
    }
    // toggle the UI with conolse UI
    public void tSetting(){
        Scanner in=new Scanner(System.in);
        System.out.println("do you want to change the user interface? (Y/n)");
        String isTrue = in.next();
        boolean temp=isTrue.equalsIgnoreCase("Y");
        if(temp){
           GUI.toggle();
        }else if(!isTrue.equalsIgnoreCase("n")){
            System.out.println("invalid input. try again");
            tSetting();
        }

    }

    /**
     * dispatch a package from the store
     */
    public void dispatch(){
         if(GUI.DEFAULT_UI==userInterface.GRAPHICAL){
             //do the graphical dipatch here
             String prompt = "what package do you went to dispatch?(insert the unique identifer-code)";
             String code = JOptionPane.showInputDialog(null, prompt,"dipatch window",JOptionPane.QUESTION_MESSAGE);
             if(!isInTheStore(code)){
                 JOptionPane.showMessageDialog(null,"the package is not found.","error",JOptionPane.ERROR_MESSAGE);
             }else{
                 Package dispachable = packageWith(code);
                 String packageInfo = "code: "+code+"\n"
                         + "name: " +dispachable.getName()+"\n"
                         + "discription: "+dispachable.getDiscription()+"\n"
                         + "price: "+dispachable.getPrice()+"\n"
                         + "amount: "+dispachable.getAmount()+"\n"
                         + "dispatched: "+dispachable.getDispatched()+"\n"
                         + "issued: "+dispachable.getIssued();
                 int amount = Integer.parseInt(JOptionPane.showInputDialog(null,"package found! \n"+packageInfo+"\n insert the amount of package to dispache."));
                 if(!dispachable.dispatch(amount))
                 {
                     JOptionPane.showMessageDialog(null,"invalid amount.","ERROR",JOptionPane.ERROR_MESSAGE);
                 }else{
                     JOptionPane.showMessageDialog(null,"successfully dispatched");
                 }
             }

         }else{
             //do the terminal dispatch
             Scanner in = new Scanner(System.in);
             String prompt = "what package do you went to dispatch?";
             System.out.println(prompt);
             String code =  in.next();
             if(!isInTheStore(code)){
                 System.out.println("the package is not found.");
             }else{
                 System.out.println("package found!");
                 Package dispachable = packageWith(code);
                 dispachable.display();
                 System.out.println("insert the amount of package to dispache.");
                 int amount = in.nextInt();
                 if(amount > dispachable.getAmount())
                 {
                     System.out.println("invalid amount.");
                 }else{
                     dispachable.dispatch(amount);
                 }
             }
         }
    }

    /**
     * search for the package in the store and return it
     * @param code code of the package
     * @return package if it is found else return null
     */
    public Package packageWith(String code){
        for(int i=0;i<top;i++){
            if(list[i].getCode().equalsIgnoreCase(code))
                return list[i];
        }
        return null;
    }
    
    /**
     * issue a package
     */
    public void issue(){
        Package newpackage = new Package();
        String displayablemsg="insert package infomation \n";
        int i=0;
        if(DEFAULT_UI!=userInterface.TERMINAL){
            try {
                String temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                if(isInTheStore(temp)){
                //package is found
                   newpackage = packageWith(temp);

                   String packageInfo = "code: "+temp+"\n"
                             + "name: " +newpackage.getName()+"\n"
                             + "discription: "+newpackage.getDiscription()+"\n"
                             + "price: "+newpackage.getPrice()+"\n"
                             + "amount: "+newpackage.getAmount()+"\n"
                             + "dispatched: "+newpackage.getDispatched()+"\n"
                             + "issued: "+newpackage.getIssued();

                     int amount = Integer.parseInt(JOptionPane.showInputDialog(null,"package found! \n"+packageInfo+"\n insert the amount of package to issue."));
                     if(newpackage.Issue(amount)){
                        JOptionPane.showMessageDialog(null,"the package has been sucessfully issued");
                     }else{
                         throw new NumberFormatException("invalid amount.");
                     }

                }else{
                    newpackage.setCode(temp);
                    displayablemsg+=FIELDS[i]+": "+temp+"\n";
                    i++;
                    temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                    newpackage.setName(temp);
                    displayablemsg+=FIELDS[i]+": "+temp+"\n";
                    i++;
                    temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                    newpackage.setDiscription(temp);
                    displayablemsg+=FIELDS[i]+": "+temp+"\n";
                    i++;
                    temp=JOptionPane.showInputDialog(null,displayablemsg +"issued "+FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                    newpackage.Issue(Long.parseLong(temp));
                    displayablemsg+=FIELDS[i]+": "+temp+"\n";
                    i++;
                    temp=JOptionPane.showInputDialog(null,displayablemsg +FIELDS[i]+"=","package registration",JOptionPane.PLAIN_MESSAGE);
                    newpackage.setPrice(Double.parseDouble(temp));
                    displayablemsg+=FIELDS[i]+": "+temp+"\n";
                    append(newpackage);
                }
             } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(null,e.getLocalizedMessage()+" please try again","error alert message",JOptionPane.ERROR_MESSAGE);
                 gEnter();
            }
        }else{
            Scanner in = new Scanner(System.in);
            System.out.print(displayablemsg +FIELDS[i]+"=");
            String temp = in.next();
            newpackage.setCode(temp);
            if(isInTheStore(temp)){
            //package is found
               newpackage = packageWith(temp);
               String packageInfo = "code: "+temp+"\n"
                         + "name: " +newpackage.getName()+"\n"
                         + "discription: "+newpackage.getDiscription()+"\n"
                         + "price: "+newpackage.getPrice()+"\n"
                         + "amount: "+newpackage.getAmount()+"\n"
                         + "dispatched: "+newpackage.getDispatched()+"\n"
                         + "issued: "+newpackage.getIssued();
                 System.out.println("package found! \n"+packageInfo+"\n insert the amount of package to issue.");
                 int amount = in.nextInt();
                 if(newpackage.Issue(amount)){
                    System.out.println("the package has been sucessfully issued");
                 }else{
                     System.out.println("invaild amount.");
                 }
            }else{
                i++;
                System.out.print(displayablemsg +FIELDS[i]+"=");
                newpackage.setName(in.next());
                i++;
                System.out.print(displayablemsg +FIELDS[i]+"=");
                newpackage.setDiscription(in.next());
                i++;
                System.out.print(displayablemsg +"issued "+FIELDS[i]+"=");
                newpackage.Issue(in.nextLong());
                i++;
                System.out.print(displayablemsg +FIELDS[i]+"=");
                newpackage.setPrice(in.nextDouble());
            }
        }
    }

    /**
     * export list of packages to a file and launch the file
     * @param type the file type to export to.
     * @throws IOException
     */
    public void exportTo(FileType type) throws IOException{
        if(top!=0){
            switch(type){
                case HTML:{
                    String HTMLData="<!DOCTYPE html><html><head><title>Departmental Store Management System</title><style>td{border: solid 2px #ddd;}tr:hover{background:#ddd;}</style></head><body><h2>list of store package for "+name+"</h2><table>\n" +
                            "<tr>	\n" +
                                "<td>code</td>	\n" +
                                "<td>name</td>\n" +
                                "<td>discretion</td>\n" +
                                "<td>quantity</td>		\n" +
                                "<td>unit price</td>\n" +
                                "<td>dispatched</td>\n" +
                                "<td>issued</td>\n" +
                            "</tr> ";
                    for(int i=0;i<top;i++){
                        HTMLData+="<tr>";
                                for(Object s : list[i].toArray()){
                                    HTMLData+="<td>"+s.toString()+"</td>\n";
                                }
                        HTMLData+="</tr>";
                    }
                    HTMLData+="</table><br/> this file is generated using store management software.</body></html>";
                    File HTMLfile = new File("html-list-exported.html");
                    FileWriter file = new FileWriter(HTMLfile);
                    file.write(HTMLData);
                    file.close();
                    Desktop.getDesktop().open(HTMLfile);
                }
                    break;
                case CSV:{
                    String CSVData="list of store package for "+name+"\ncode,name,discretion,quantity,unit price,dispatched,issued\n";
                    for(int i=0;i<top;i++){
                                for(Object s : list[i].toArray()){
                                    CSVData+=s.toString()+",";
                                }
                        CSVData+="\n";
                    }
                    CSVData+="\nthis file is generated using store management software.";
                    File CSVfile = new File("csv-list-exported.csv");
                    FileWriter file = new FileWriter(CSVfile);
                    file.write(CSVData);
                    file.close();
                    Desktop.getDesktop().open(CSVfile);
                }
                    break;
                case TXT:{
                    String TXTData="list of store package for "+name+"\r\n"+toString();
                    TXTData+="\r\nthis file is generated using store management software.";
                    File TXTfile = new File("txt-list-exported.txt");
                    FileWriter file = new FileWriter(TXTfile);
                    file.write(TXTData);
                    file.close();
                    Desktop.getDesktop().open(TXTfile);
                }
                    break;
            }
        }else{
            // do not export an empty list
            if(DEFAULT_UI==userInterface.GRAPHICAL){
                JOptionPane.showMessageDialog(null,"can not export an empty store.","error message",JOptionPane.ERROR_MESSAGE);
            }else{
                System.out.println("error: can not export an empty stroe.");
            }
        }
    }
    
    /**
     * save the current sore object to a binary file.
     * @param FileName the file name to save the object to.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveStoreTo(String FileName) throws FileNotFoundException, IOException{
        File StoreData= new File(FileName);
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(StoreData));
        ois.writeObject(this);
    }

    /**
     * generate or create a report from the current list of packages
     * @return Report object with package list
     */
    public Report toReport(){
        if(top>0){
        Package[] plist = new Package[top];
        System.arraycopy(list,0,plist,0,top);
        return new Report(plist,"Store report for "+name,"");
        }
        return null;
    }
}
