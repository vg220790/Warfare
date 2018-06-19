package SharedInterface;

import GraphicsContent.GraphicsApplication;
import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import JSONParser.WarParser;
import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleApplication implements Runnable  {
    private GraphicsApplication graphicsApplication;
    private WarInterface warInterface;
    private boolean isWarRunning;
    private Runnable mainProgram;


    @Override
    public void run() {
        isWarRunning = false;
        warInterface = new WarParser();
       // parsedEntities = new WarParser();
        int decision = 0;
        System.out.println("Would you like to load config from JSON? 1 - yes, 2 - no");
        Scanner scanner = new Scanner(System.in);

        while (decision != 1 && decision != 2) {
            decision = scanner.nextInt();
            if(decision != 1 && decision != 2)
                System.out.println("Wrong input!! Would you like to load config from JSON? 1 - yes, 2 - no");
        }
        if(decision == 1) {
            File configuration = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open Resource File");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                configuration = fileChooser.getSelectedFile();
            }
            if(configuration != null)
            {
                Gson gson = new Gson();

                try {
                    warInterface = gson.fromJson(new FileReader(configuration), WarParser.class);
                    SystemTime time = new MyTime();
                    Thread timeThread = new Thread(time);
                    timeThread.start();

                    isWarRunning = true;
                    this.graphicsApplication = new GraphicsApplication(time, warInterface);
                    mainProgram = new MainLogic(time, graphicsApplication,(WarParser)warInterface);
                    Thread mainThread = new Thread(mainProgram);
                    mainThread.start();




                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }

            decision = -1;
            boolean success = false;
            String launcherId;
            int numAmount = 0;


            while (decision < 0 || decision > 9){
                System.out.println("1 - Add Missile Launcher");
                System.out.println("2 - Add Missile Launcher Destructor");
                System.out.println("3 - Add Missile Destructor");
                System.out.println("4 - Launch Missile");
                System.out.println("5 - Put Destroy Missile Launcher Command");
                System.out.println("6 - Put Destroy Missile Command");
                System.out.println("7 - Show Stats");
                System.out.println("8 - Exit");
                if(!isWarRunning)
                    System.out.println("0 - Start War");
                String deci = scanner.next();
                while(!isNumeric(deci)){
                    System.out.println("Choice must be numerical!!!");
                    deci = scanner.next();
                }
                decision =  Integer.parseInt(deci);

                if(!isWarRunning)
                    while(decision < 0 || decision > 8) {
                        System.out.println("Wrong choice!!!");
                        decision = scanner.nextInt();
                }
                else
                    while(decision < 1 || decision > 8) {
                        System.out.println("Wrong choice!!!");
                        decision = scanner.nextInt();
                    }


        switch (decision){
            case 1:

                while(!success) {
                    System.out.println("Launcher's Id: ONLY NUMBERS ALLOWED");
                    launcherId = scanner.next();
                    while(!isNumeric(launcherId)){
                        System.out.println("Launcher's Id to add missiles to: ONLY NUMBERS ALLOWED");
                        launcherId = scanner.next();
                    }
                    launcherId = "L"+launcherId;
                    System.out.println("is Hidden? Type 'yes' for hidden, anything else for not hidden");
                    String hidden = scanner.next();
                    boolean isHidden;
                    isHidden = hidden.equals("yes");
                    ML temp = new ML(launcherId, isHidden, null);


                    success = warInterface.addMissileLauncher(temp);

                    if(!success)
                        System.out.println("Launcher exist/wrong fields!!!");
                    else {
                        System.out.println(temp.getId() + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addWarEntity(temp);
                    }

                }
                decision = -1;
                success = false;
                break;
            case 2:

                while(!success) {
                    System.out.println("Type: type 'plane' for Plane, or anything else for Battleship");
                    String type = scanner.next();
                    LD temp = new LD(type, null);
                    success = warInterface.addMissileLauncherDestructor(temp);
                    if(!success)
                        System.out.println("Launcher Destructor exist/wrong fields!!!");
                    else {
                        System.out.println(temp.getId() + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addWarEntity(temp);
                    }

                }
                success = false;
                decision = -1;
                break;
            case 3:
                while(!success) {
                    System.out.println("Missile Destructor Id: ONLY NUMBERS ALLOWED!!");
                    launcherId = scanner.next();
                    while(!isNumeric(launcherId)){
                        System.out.println("Missile Destructor Id: ONLY NUMBERS ALLOWED!!");
                        launcherId = scanner.next();
                    }
                    launcherId = "MD" + launcherId;
                    MD temp = new MD(launcherId,null);

                    success = warInterface.addMissileDestructor(temp);

                    if(!success)
                        System.out.println("Missile Destructor exist/wrong fields!!!");
                    else {
                        System.out.println(temp.getId() + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addWarEntity(temp);
                    }

                }
                decision = -1;
                success = false;
                break;
            case 4:
                System.out.println("Launcher's Id to add missiles to: ONLY NUMBERS ALLOWED");
                launcherId = scanner.next();
                while(!isNumeric(launcherId)){
                    System.out.println("Launcher's Id to add missiles to: ONLY NUMBERS ALLOWED");
                    launcherId = scanner.next();
                }
                launcherId = "L" + launcherId;
                String amount = "";
                while(!isNumeric(amount)){
                    System.out.println("Amount of missiles to add: (1-3)");
                    amount = scanner.next();
                    if(isNumeric(amount)) {

                        numAmount = Integer.parseInt(amount);

                        if (numAmount < 1 || numAmount > 3) {
                            System.out.println("Wrong amount!!!! (1-3)");
                            amount = "";
                        }
                    }
                }
                for(int i = 0 ; i < numAmount ; i ++) {
                    System.out.println("Enter Missile Id: ONLY NUMBERS ALLOWED!!");
                    String mId = scanner.next();
                    while(!isNumeric(mId)){
                        System.out.println("Enter Missile Id: ONLY NUMBERS ALLOWED!!");
                        mId = scanner.next();
                    }
                    mId = "M" + mId;

                    System.out.println("Enter Missile destination: type b/n/r/o/s = Beer-Sheva/Netivot/Rahat/Ofakim/Sderot");
                    String destination = scanner.next();
                    while(!destination.equals("b") && !destination.equals("n") && !destination.equals("r") && !destination.equals("o") && !destination.equals("s")){
                        System.out.println("Enter Missile destination: type b/n/r/o/s = Beer-Sheva/Netivot/Rahat/Ofakim/Sderot");
                        destination = scanner.next();
                    }
                    switch (destination){
                        case "b":
                            destination = "Beer-Sheva";
                            break;
                        case "n":
                            destination = "Netivot";
                            break;
                        case "r":
                            destination = "Rahat";
                            break;
                        case "o":
                            destination = "Ofakim";
                            break;
                        case "s":
                            destination = "Sderot";
                            break;
                    }
                    System.out.println("Missile launch time: ONLY NUMBERS ALLOWED!!");
                    String launchTime = scanner.next();
                    while(!isNumeric(launchTime)) {
                        System.out.println("Missile launch time: ONLY NUMBERS ALLOWED!!");
                        launchTime = scanner.next();
                    }
                    System.out.println("Missile fly time: ONLY NUMBERS ALLOWED!!");
                    String flyTime = scanner.next();
                    while(!isNumeric(flyTime)) {
                        System.out.println("Missile fly time: ONLY NUMBERS ALLOWED!!");
                        flyTime = scanner.next();
                    }
                    System.out.println("Missile's potential damage: ONLY NUMBERS ALLOWED!!");
                    String damage = scanner.next();
                    while(!isNumeric(flyTime)) {
                        System.out.println("Missile's potential damage: ONLY NUMBERS ALLOWED!!");
                        flyTime = scanner.next();
                    }
                    M tempMissile = new M(mId, destination, launchTime, flyTime, damage);
                   success =  warInterface.addMissile(launcherId, tempMissile);

                    if(!success)
                        System.out.println("Missile exist/wrong fields!!!");
                    else {
                        System.out.println(tempMissile.getId() + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addMissileEntity(launcherId,tempMissile);
                    }
                }
                success = false;
                decision = -1;
                break;
            case 5:
                while(!success) {
                    System.out.println("Missile Launcher Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
                    launcherId = scanner.next();
                    while(!isNumeric(launcherId)) {
                        System.out.println("Missile Launcher Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
                        launcherId = scanner.next();
                    }
                    launcherId = "LD" + launcherId;
                    System.out.println("Missile Launcher Id to destroy: ONLY NUMBERS ALLOWED!!");
                    String destId = scanner.next();
                    while(!isNumeric(destId)) {
                        System.out.println("Missile Launcher Id to destroy: ONLY NUMBERS ALLOWED!!");
                        destId = scanner.next();
                    }
                    destId = "L" + destId;
                    System.out.println("Destruct Time: ONLY NUMBERS ALLOWED!!");
                    String destTime = scanner.next();
                    while(!isNumeric(destTime)) {
                        System.out.println("Destruct Time: ONLY NUMBERS ALLOWED!!");
                        destTime = scanner.next();
                    }

                    DestLauncher temp = new DestLauncher(destId, destTime);
                    success = warInterface.addDestLauncher(launcherId,temp);
                    if(!success)
                        System.out.println("Operation failed (Destructor doesn't exist?)!!!");
                    else {
                        System.out.println("Operation for Destructor " + launcherId + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addDestLauncherCommand(launcherId,Integer.parseInt(destTime),destId);
                    }

                }
                success = false;
                decision = -1;
                break;
            case 6:
                while(!success) {
                    System.out.println("Missile Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
                    launcherId = scanner.next();
                    while(!isNumeric(launcherId)) {
                        System.out.println("Missile Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
                        launcherId = scanner.next();
                    }
                    launcherId = "MD" + launcherId;
                    System.out.println("Missile Id to destroy: ONLY NUMBERS ALLOWED!!");
                    String destId = scanner.next();
                    while(!isNumeric(destId)) {
                        System.out.println("Missile Id to destroy: ONLY NUMBERS ALLOWED!!");
                        destId = scanner.next();
                    }
                    destId = "M" + destId;
                    System.out.println("Destruct Time After Launch: ONLY NUMBERS ALLOWED!!");
                    String destTime = scanner.next();
                    while(!isNumeric(destTime)) {
                        System.out.println("Destruct Time After Launch: ONLY NUMBERS ALLOWED!!");
                        destTime = scanner.next();
                    }

                    DestMissile temp = new DestMissile(destId, destTime);
                    success = warInterface.addDestMissile(launcherId,temp);
                    if(!success)
                        System.out.println("Operation failed (Destructor doesn't exist?)!!!");
                    else {
                        System.out.println("Operation for Destructor " + launcherId + " Added!!");
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addDestMissileCommand(launcherId,Integer.parseInt(destTime),destId);
                    }

                }
                success = false;
                decision = -1;
                break;
            case 7:
                break;
            case 8:
                if(isWarRunning)
                    ((MainLogic)mainProgram).haltSystem();

                break;
            case 0:
                isWarRunning = true;
                SystemTime time = new MyTime();
                Thread timeThread = new Thread(time);
                timeThread.start();

                this.graphicsApplication = new GraphicsApplication(time,warInterface);
                mainProgram = new MainLogic(time, graphicsApplication,(WarParser)warInterface);
                Thread mainThread = new Thread(mainProgram);
                mainThread.start();
                decision = -1;
                break;
        }

            }


    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
