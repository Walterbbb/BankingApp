package guis;

import DataBase_Objects.User;

import javax.swing.*;
import java.awt.*;

/*
    Creating an abstract class helps us setup the blueprint that our GUIs will follow, for example
    in each of the guis they will be the same size and will need to invoke their own addGuiComponents()
    which will be unique to each subclass
 */
public abstract class BaseFrame extends JFrame {
    protected User user;

    public BaseFrame(String title){
        initialize(title);
    }
    public BaseFrame(String title, User user){
        //initialize user
        this.user = user;
        initialize(title);
    }
    private void initialize(String title){
        // instantiate iFrame properties and add a title to the bar
        setTitle(title);

        //set size(in pixels)
        setSize(400,600);

        //terminate program when the gui is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set layout to null to have absolute layout which allows us to manually specify the size and position of each gui component
        setLayout(null);

        //prevent gui from being resized
        setResizable(false);

        //Launch the gui in the center of the screen
        setLocationRelativeTo(null);

        //call on the subclass addGuiComponent
        addGuiComponents();
    }
    //this method will need to be defined by subclasses when this class is being iterated from
    protected abstract void addGuiComponents();

}
