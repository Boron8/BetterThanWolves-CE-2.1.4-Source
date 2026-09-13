package com.prupe.mcpatcher;

import java.util.HashMap;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MCLogger {
   private static final HashMap<String, MCLogger> allLoggers = new HashMap<>();
   public static final Level ERROR = new MCLogger.ErrorLevel();
   private static final long FLOOD_INTERVAL = 1000L;
   private static final long FLOOD_REPORT_INTERVAL = 5000L;
   private static final int FLOOD_LIMIT = Config.getInstance().floodMessageLimit;
   private static final int FLOOD_LEVEL = Level.INFO.intValue();
   private final String logPrefix;
   private final Logger logger;
   private boolean flooding;
   private long lastFloodReport;
   private int floodCount;
   private long lastMessage = System.currentTimeMillis();
   private long lastLogEvery = this.lastMessage;

   public static MCLogger getLogger(String category) {
      return getLogger(category, category);
   }

   public static synchronized MCLogger getLogger(String category, String logPrefix) {
      MCLogger logger = allLoggers.get(category);
      if (logger == null) {
         logger = new MCLogger(category, logPrefix);
         allLoggers.put(category, logger);
      }

      return logger;
   }

   private MCLogger(String category, String logPrefix) {
      this.logPrefix = logPrefix;
      this.logger = Logger.getLogger(category);
      this.logger.setLevel(Config.getLogLevel(category));
      this.logger.setUseParentHandlers(false);
      this.logger.addHandler(new Handler() {
         private final Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
               Level level = record.getLevel();
               if (level == Level.CONFIG) {
                  return record.getMessage();
               } else {
                  String message = record.getMessage();

                  String prefix;
                  for (prefix = ""; message.startsWith("\n"); message = message.substring(1)) {
                     prefix = prefix + "\n";
                  }

                  return prefix + "[" + MCLogger.this.logPrefix + "] " + level.toString() + ": " + message;
               }
            }
         };

         @Override
         public void publish(LogRecord record) {
            System.out.println(this.formatter.format(record));
         }

         @Override
         public void flush() {
         }

         @Override
         public void close() throws SecurityException {
         }
      });
   }

   private boolean checkFlood() {
      long now = System.currentTimeMillis();
      boolean showFloodMessage = false;
      if (now - this.lastMessage > 1000L) {
         if (this.flooding) {
            this.reportFlooding(now);
            this.flooding = false;
         } else {
            this.floodCount = 0;
         }
      } else if (this.flooding && now - this.lastFloodReport > 5000L) {
         this.reportFlooding(now);
         showFloodMessage = true;
      }

      this.lastMessage = now;
      this.floodCount++;
      if (this.flooding) {
         return showFloodMessage;
      } else if (this.floodCount > FLOOD_LIMIT) {
         this.flooding = true;
         this.lastFloodReport = now;
         this.reportFlooding(now);
         return false;
      } else {
         return true;
      }
   }

   private void reportFlooding(long now) {
      if (this.floodCount > 0) {
         this.logger.log(Level.WARNING, String.format("%d flood messages dropped in the last %ds", this.floodCount, (now - this.lastFloodReport) / 1000L));
      }

      this.floodCount = 0;
      this.lastFloodReport = now;
   }

   public boolean isLoggable(Level level) {
      return this.logger.isLoggable(level);
   }

   public void setLevel(Level level) {
      this.logger.setLevel(level);
   }

   public void log(Level level, String format, Object... params) {
      if (this.isLoggable(level)) {
         if (level.intValue() >= FLOOD_LEVEL && !this.checkFlood()) {
            return;
         }

         this.logger.log(level, String.format(format, params));
      }
   }

   public void severe(String format, Object... params) {
      this.log(Level.SEVERE, format, params);
   }

   public void error(String format, Object... params) {
      this.log(ERROR, format, params);
   }

   public void warning(String format, Object... params) {
      this.log(Level.WARNING, format, params);
   }

   public void info(String format, Object... params) {
      this.log(Level.INFO, format, params);
   }

   public void config(String format, Object... params) {
      this.log(Level.CONFIG, format, params);
   }

   public void fine(String format, Object... params) {
      this.log(Level.FINE, format, params);
   }

   public void finer(String format, Object... params) {
      this.log(Level.FINER, format, params);
   }

   public void finest(String format, Object... params) {
      this.log(Level.FINEST, format, params);
   }

   public boolean logEvery(long milliseconds) {
      long now = System.currentTimeMillis();
      if (now - this.lastLogEvery > milliseconds) {
         this.lastLogEvery = now;
         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   private static class ErrorLevel extends Level {
      protected ErrorLevel() {
         super("ERROR", (Level.WARNING.intValue() + Level.SEVERE.intValue()) / 2);
      }
   }
}
