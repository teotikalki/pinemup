/*
 * pin 'em up
 * 
 * Copyright (C) 2007 by Mario Koedding
 *
 *
 * pin 'em up is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * pin 'em up is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with pin 'em up; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.sourceforge.pinemup.logic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PinEmUp {
   private static final String VERSION = "0.3-svn";
   
   public Note notes, failNote;

   private static PinEmUp main;

   private static UserSettings settings;

   private static JFileChooser fileDialog;

   private TrayMenu menu;

   public static JFileChooser getFileDialog() {
      return fileDialog;
   }
   
   public TrayMenu getTrayMenu() {
      return menu;
   }

   public static void setPinEmUp(PinEmUp m) {
      main = m;
   }

   public static PinEmUp getMainApp() {
      return main;
   }

   public Note getFailNote() {
      return failNote;
   }

   public static void setUserSettings(UserSettings s) {
      settings = s;
   }

   public static UserSettings getUserSettings() {
      return settings;
   }

   public void exit() {
      // save notes to file
      NoteIO.writeNotesToFile(getNotes(), PinEmUp.getUserSettings().getNotesFile());
      
      System.exit(0);
   }

   public PinEmUp() {
      TrayIcon icon = null;

      if (SystemTray.isSupported()) {
         // for FTP issues
         failNote = new Note();
         
         PinEmUp.setPinEmUp(this);

         // load user settings
         PinEmUp.setUserSettings(new UserSettings());

         SystemTray tray = SystemTray.getSystemTray();

         Image img = ResourceLoader.getTrayIcon();

         // create popup menu
         menu = new TrayMenu(new TrayMenuLogic());

         // create trayicon
         icon = new TrayIcon(img, "pin 'em up", menu);
         icon.setImageAutoSize(true);
         
         // add actionlistener for doubleclick on icon
         icon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               PinEmUp.getMainApp().setNotes(Note.add(PinEmUp.getMainApp().getNotes(), ""));
               PinEmUp.getMainApp().getNotes().showAllVisible();
            }
         });

         // add trayicon
         try {
            tray.add(icon);
         } catch (AWTException e) {
            System.err.println(e);
         }

         // load notes from file
         notes = NoteIO.readNotesFromFile(PinEmUp.getUserSettings().getNotesFile());

         // show all visible notes
         if (notes != null) {
            notes.showAllVisible();
         }
         
         // create File-Dialog
         fileDialog = new JFileChooser();
         fileDialog.removeChoosableFileFilter(fileDialog.getChoosableFileFilters()[0]);
         fileDialog.setFileFilter(new MyFileFilter("TXT"));
         fileDialog.setMultiSelectionEnabled(false);
      } else {
         JOptionPane.showMessageDialog(null, "Error! TrayIcon not supported by your system. Exiting...", "pin 'em up - error", JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   public Note getNotes() {
      return notes;
   }

   public void setNotes(Note n) {
      notes = n;
   }

   public static void main(String args[]) {
      new PinEmUp();
   }
   
   public static String getVersion() {
      return VERSION;
   }
}