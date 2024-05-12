package org.example.backend.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.backend.model.MyUser;
import org.example.backend.model.Remind;
import org.example.backend.repository.FileWriterAndLoader;
import org.example.backend.statics.PathConstants;
import org.example.bot.maker.MessageMaker;

import java.util.List;
import java.util.Objects;

public class RemindService implements PathConstants {
   private FileWriterAndLoader<Remind> fileWriterOrLoader;

    public RemindService() {
        this.fileWriterOrLoader = new FileWriterAndLoader<>(REMINDS_PATH);
    }

    public void save(Remind remind){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            if(Objects.equals(remind,reminds.get(i))){
              reminds.set(i,remind);
              fileWriterOrLoader.write(reminds);
              return;
            }

        }
        reminds.add(remind);
        fileWriterOrLoader.write(reminds);
    }
    public void changeRemind(Remind remind){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            if(Objects.equals(remind.getUserId(),reminds.get(i).getUserId())){
                reminds.set(i,remind);
                fileWriterOrLoader.write(reminds);
                return;
            }

        }
        reminds.add(remind);
        fileWriterOrLoader.write(reminds);
    }
    public int getByIndex(Long id, int index){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        int count=0;
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                count++;
                if (index==count) {
                    return i;
                }
            }
        }
        return -1;
    }
    public Remind get(Long id){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                return cur;
            }
        }
        return null;
    }
    public boolean deleteByindex(int index){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        reminds.remove(index);
        fileWriterOrLoader.write(reminds);
        return true;
    }


    public boolean sendAllReminds(Long id, TelegramBot bot){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        int count=0;
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                SendMessage sendMessage1 = new SendMessage(id, "🗓️Choose what you want to delete");
                bot.execute(sendMessage1);
                count++;
                SendMessage sendMessage= new SendMessage(id,count+": "+cur.getText()+" "+cur.getSendDate());
                bot.execute(sendMessage);
            }
        }

        return get(id) == null;
    }


}
