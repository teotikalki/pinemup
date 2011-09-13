/*
 * pin 'em up
 *
 * Copyright (C) 2007-2011 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.gui.I18N;
import net.sourceforge.pinemup.logic.UserSettings;

public class FTPConnection extends ServerConnection {

   @Override
   public void importNotesFromServer() {
      boolean downloaded = true;
      try {
         makeBackupFile();
         File f = new File(UserSettings.getInstance().getNotesFile());
         FileOutputStream fos = new FileOutputStream(f);
         String filename = f.getName();
         String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":"
               + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress()
               + UserSettings.getInstance().getServerDir() + filename + ";type=i";
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         InputStream is = urlc.getInputStream();
         int nextByte = is.read();
         while (nextByte != -1) {
            fos.write(nextByte);
            nextByte = is.read();
         }
         fos.close();
      } catch (IOException e) {
         downloaded = false;
      }
      if (downloaded) {
         deleteBackupFile();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfiledownloaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      } else {
         restoreFileFromBackup();
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotdownloaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
   }

   @Override
   public void exportNotesToServer() {
      boolean uploaded = true;
      String completeFilename = UserSettings.getInstance().getNotesFile();
      File f = new File(completeFilename);
      String filename = f.getName();
      String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":"
      + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress()
      + UserSettings.getInstance().getServerDir() + filename + ";type=i";
      FileInputStream fis;
      try {
         fis = new FileInputStream(f);
         URL url = new URL(ftpString);
         URLConnection urlc = url.openConnection();
         OutputStream  os = urlc.getOutputStream();

         int nextByte = fis.read();
         while (nextByte != -1) {
            os.write(nextByte);
            nextByte = fis.read();
         }
         fis.close();
         os.close();
      } catch (IOException e) {
         uploaded = false;
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotuploaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
      }
      if (uploaded) {
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfileuploaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
      }
   }
}
