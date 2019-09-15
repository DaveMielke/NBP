package org.nbp.common.dictionary;

public abstract class ResponseCodes {
  private ResponseCodes () {
  }

  public final static int TYPE_POSITIVE_PRELIMINARY  = 100;
  public final static int TYPE_POSITIVE_COMPLETE     = 200;
  public final static int TYPE_POSITIVE_INTERMEDIATE = 300;
  public final static int TYPE_NEGATIVE_TRANSIENT    = 400;
  public final static int TYPE_NEGATIVE_PERMANENT    = 500;

  public final static int CATEGORY_SYNTAX         =  0;
  public final static int CATEGORY_INFORMATIOPN   = 10;
  public final static int CATEGORY_CONNECTION     = 20;
  public final static int CATEGORY_AUTHENTICATION = 30;
  public final static int CATEGORY_SYSTEm         = 50;
  public final static int CATEGORY_PRIVATE        = 80;

  public final static int BEGIN_DATABASE_LIST = 110; // n databases present - text follows
  public final static int BEGIN_STRATEGY_LIST = 111; // n strategies available - text follows
  public final static int BEGIN_DATABASE_TEXT = 112; // database information follows
  public final static int BEGIN_HELP_TEXT = 113; // help text follows
  public final static int BEGIN_SERVER_TEXT = 114; // server information follows
  public final static int BEGIN_AUTHENTICATION_CHALLENGE = 130; // challenge follows
  public final static int BEGIN_DEFINITION_LIST = 150; // n definitions retrieved - definitions follow
  public final static int BEGIN_DEFINITION_TEXT = 151; // word database name - text follows
  public final static int BEGIN_MATCH_LIST = 152; // n matches found - text follows

  public final static int SERVER_STATUS = 210; // (optional timing and statistical information here)
  public final static int SERVER_BANNER = 220; // text msg-id
  public final static int SERVER_DISCONNECTING = 221; // Closing Connection
  public final static int AUTHENTICATION_SUCCESSFUL = 230; // Authentication successful
  public final static int RESPONSE_COMPLETE = 250; // ok (optional timing information here)

  public final static int AUTHENTICATION_REQUESTED = 330; // send response

  public final static int SERVER_OFFLINE_TRANSIENT = 420; // Server temporarily unavailable
  public final static int SERVER_OFFLINE_PERMANENT = 421; // Server shutting down at operator request

  public final static int UNKNOWN_COMMAND = 500; // Syntax error, command not recognized
  public final static int ILLEGAL_PARAMETER = 501; // Syntax error, illegal parameters
  public final static int UNIMPLEMENTED_COMMAND = 502; // Command not implemented
  public final static int UNIMPLEMENTED_PARAMETER = 503; // Command parameter not implemented
  public final static int CLIENT_REJECTED = 530; // Access denied
  public final static int AUTHENTICATION_FAILED = 531; // Access denied, use "SHOW INFO" for server information
  public final static int UNKNOWN_MECHANISM = 532; // Access denied, unknown mechanism
  public final static int UNKNOWN_DATABASE = 550; // Invalid database, use "SHOW DB" for list of databases
  public final static int UNKNOWN_STRATEGY = 551; // Invalid strategy, use "SHOW STRAT" for a list of strategies
  public final static int NO_MATCH = 552; // No match
  public final static int NO_DATABASES = 554; // No databases present
  public final static int NO_STRATEGIES = 555; // No strategies available
}
