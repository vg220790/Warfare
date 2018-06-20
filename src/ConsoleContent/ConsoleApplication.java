package ConsoleContent;

import GraphicsContent.GraphicsApplication;
import SharedInterface.WarImp;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Entities.BaseEntities.Missile;
import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;
import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import com.google.gson.Gson;
import javafx.geometry.Point2D;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleApplication implements Runnable  {
    private Runnable mainProgram;
    private SystemTime time;


    @Override
    public void run() {
        boolean isWarRunning = false;
        WarInterface warInterface = new WarImp();
        String decisionStr = "";
        int decision = 0;
        Scanner scanner = new Scanner(System.in);

        while(decision != 1 && decision != 2) {
            while (!isNumeric(decisionStr)) {
                System.out.println("Would you like to load config from JSON? 1 - yes, 2 - no");
                decisionStr = scanner.next();
            }
            decision = Integer.parseInt(decisionStr);
            if(decision != 1 && decision != 2)
                decisionStr = "";
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
                    warInterface = gson.fromJson(new FileReader(configuration), WarImp.class);
                    time = new MyTime();
                    Thread timeThread = new Thread(time);
                    timeThread.start();

                    isWarRunning = true;
                    GraphicsApplication graphicsApplication = new GraphicsApplication(time, warInterface);
                    mainProgram = new MainLogic(time, graphicsApplication,warInterface);
                    Thread mainThread = new Thread(mainProgram);
                    mainThread.start();




                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }

            decision = 9999;
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
                    MissileLauncher temp = new MissileLauncher(launcherId, isHidden, null);


                    success = warInterface.addMissileLauncher(temp);

                    if(!success)
                        System.out.println("Launcher exist/wrong fields!!!");
                    else {
                        if(isWarRunning)
                            ((MainLogic)mainProgram).addWarEntity(temp);        // If the war runs, start the thread right away.
                        System.out.println(temp.getId() + " Added!!");
                    }

                }
                decision = -1;
                success = false;
                break;
            case 2:

                while(!success) {
                    System.out.println("Type: type 'plane' for Plane, or anything else for Battleship");
                    String type = scanner.next();
                    MissileLauncherDestructor temp = new MissileLauncherDestructor(type, null);
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
                    MissileDestructor temp = new MissileDestructor(launcherId,null);

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
                boolean launcherExist;
               launcherId = "";
                while(!isNumeric(launcherId)){
                    System.out.println("Launcher's Id to add missiles to: ONLY NUMBERS ALLOWED");
                    launcherId = scanner.next();

                launcherExist = (warInterface.getLauncherById("L"+launcherId) != null);
                    if(!launcherExist){
                        System.out.println("Launcher With that ID doesn't exists!");
                        launcherId = "";
                    }

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
                    String destinationStr = scanner.next();
                    while(!destinationStr.equals("b") && !destinationStr.equals("n") && !destinationStr.equals("r") && !destinationStr.equals("o") && !destinationStr.equals("s")){
                        System.out.println("Enter Missile destination: type b/n/r/o/s = Beer-Sheva/Netivot/Rahat/Ofakim/Sderot");
                        destinationStr = scanner.next();
                    }
                    switch (destinationStr){
                        case "b":
                            destinationStr = "Beer-Sheva";
                            break;
                        case "n":
                            destinationStr = "Netivot";
                            break;
                        case "r":
                            destinationStr = "Rahat";
                            break;
                        case "o":
                            destinationStr = "Ofakim";
                            break;
                        case "s":
                            destinationStr = "Sderot";
                            break;
                    }
                    System.out.println("Missile launch time: ONLY NUMBERS ALLOWED!!");
                    String launchTimeStr = scanner.next();
                    while(!isNumeric(launchTimeStr)) {
                        System.out.println("Missile launch time: ONLY NUMBERS ALLOWED!!");
                        launchTimeStr = scanner.next();
                    }
                    System.out.println("Missile fly time: ONLY NUMBERS ALLOWED!!");
                    String flyTimeStr = scanner.next();
                    while(!isNumeric(flyTimeStr)) {
                        System.out.println("Missile fly time: ONLY NUMBERS ALLOWED!!");
                        flyTimeStr = scanner.next();
                    }
                    System.out.println("Missile's potential damage: ONLY NUMBERS ALLOWED!!");
                    String damageStr = scanner.next();
                    while(!isNumeric(flyTimeStr)) {
                        System.out.println("Missile's potential damage: ONLY NUMBERS ALLOWED!!");
                        flyTimeStr = scanner.next();
                    }
                    Point2D coordinates = warInterface.getTargetByName(destinationStr).getCoordinates();
                    int launchTime = Integer.parseInt(launchTimeStr);
                    int flyTime = Integer.parseInt(flyTimeStr);
                    int damage = Integer.parseInt(damageStr);


                    Missile tempMissile = new Missile(mId, coordinates, launchTime, flyTime, damage,time);
                    tempMissile.setDestination(destinationStr);
                    tempMissile.setCoordinates(warInterface.getLauncherById(launcherId).getCoordinates());
                    success =  warInterface.addMissile(launcherId, tempMissile);
                    if(!success)
                        System.out.println("Missile exist/wrong fields!!!");
                    else {
                        System.out.println(tempMissile.getId() + " Added!!");
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

                    warInterface.addDestLauncher(launcherId,destId,Integer.parseInt(destTime));
                    success = true;
                    System.out.println("Operation for Destructor " + launcherId + " Added!!");
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
                    warInterface.addDestMissile(launcherId,destId,Integer.parseInt(destTime));
                    System.out.println("Operation for Destructor " + launcherId + " Added!!");
                    success = true;
                }
                success = false;
                decision = -1;
                break;
            case 7:
                break;
            case 8:
                if(isWarRunning)
                    warInterface.haltSystem();

                break;
            case 0:
                isWarRunning = true;
                time = new MyTime();
                Thread timeThread = new Thread(time);
                timeThread.start();

                GraphicsApplication graphicsApplication = new GraphicsApplication(time,warInterface);
                mainProgram = new MainLogic(time, graphicsApplication,warInterface);
                Thread mainThread = new Thread(mainProgram);
                mainThread.start();
                decision = -1;
                break;
        }

            }


    }

    private static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
