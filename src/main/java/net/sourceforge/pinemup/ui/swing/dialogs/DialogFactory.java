package net.sourceforge.pinemup.ui.swing.dialogs;

import net.sourceforge.pinemup.core.io.UpdateCheckResultHandler;
import net.sourceforge.pinemup.core.UserInputRetriever;

public class DialogFactory {
   private UserInputRetriever userInputRetriever;
   private UpdateCheckResultHandler updateCheckResultHandler;

   private SettingsDialog settingsDialogInstance;

   public DialogFactory(UserInputRetriever userInputRetriever,
         UpdateCheckResultHandler updateCheckResultHandler) {
      this.userInputRetriever = userInputRetriever;
      this.updateCheckResultHandler = updateCheckResultHandler;
   }

   public void showSettingsDialog() {
      if (settingsDialogInstance == null || !settingsDialogInstance.isVisible()) {
         settingsDialogInstance = new SettingsDialog(userInputRetriever, updateCheckResultHandler);
      }
   }
}
