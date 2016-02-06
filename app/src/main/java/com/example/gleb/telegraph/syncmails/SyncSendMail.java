package com.example.gleb.telegraph.syncmails;

import com.example.gleb.telegraph.abstracts.AbstractSyncMails;
import com.example.gleb.telegraph.models.MailFolder;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 * Created by gleb on 06.02.16.
 */
public class SyncSendMail extends AbstractSyncMails {

    /**
     * Sync mails with folder Send
     * @param Store               Store with mails
     * @param MimeMessage         Message for sync
     * @return Properties         Properties for imap protocol
     * */
    @Override
    public void syncMail(Store syncStore, MimeMessage msg) {
        try {
            Folder[] folders = syncStore.getDefaultFolder().list();
            for (Folder folder : folders){
                if (MailFolder.isSendFolder(folder)){
                    folder.open(Folder.READ_WRITE);
                    MimeMessage message = msg;
                    folder.appendMessages(new Message[]{message});
                    folder.close(true);
                    syncStore.close();
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
