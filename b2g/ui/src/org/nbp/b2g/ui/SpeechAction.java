package org.nbp.b2g.ui;

public abstract class SpeechAction extends Action {
  protected SpeechDevice getSpeechDevice () {
    return Devices.getSpeechDevice();
  }

  private abstract class SpeechControl {
    protected abstract String getLabel ();
    protected abstract float getScale ();
    protected abstract float getValue (SpeechDevice speech);
    protected abstract boolean setValue (SpeechDevice speech, float value);

    protected float toInternal (float value) {
      return value;
    }

    protected float toExternal (float value) {
      return value;
    }

    public boolean adjustControl (SpeechDevice speech, int steps) {
      float scale = getScale();
      int oldValue = Math.round(toInternal(getValue(speech)) * scale);

      int newValue = oldValue + steps;
      if (!setValue(speech, toExternal(newValue / scale))) return false;

      message(getLabel(), newValue);
      return true;
    }
  }

  private abstract class LinearSpeechControl extends SpeechControl {
    @Override
    protected float getScale () {
      return 10.0f;
    }
  }

  private abstract class LogarithmicSpeechControl extends SpeechControl {
    @Override
    protected float toInternal (float value) {
      return (float)Math.log10(value);
    }

    @Override
    protected float toExternal (float value) {
      return (float)Math.pow(10.0, value);
    }

    @Override
    protected float getScale () {
      return 30.0f;
    }
  }

  protected boolean adjustVolume (SpeechDevice speech, int steps) {
    SpeechControl control = new LinearSpeechControl() {
      @Override
      protected String getLabel () {
        return "volume";
      }

      @Override
      protected float getValue (SpeechDevice speech) {
        return speech.getVolume();
      }

      @Override
      protected boolean setValue (SpeechDevice speech, float value) {
        return speech.setVolume(value);
      }
    };

    return control.adjustControl(speech, steps);
  }

  protected boolean adjustBalance (SpeechDevice speech, int steps) {
    SpeechControl control = new LinearSpeechControl() {
      @Override
      protected String getLabel () {
        return "balance";
      }

      @Override
      protected float getValue (SpeechDevice speech) {
        return speech.getBalance();
      }

      @Override
      protected boolean setValue (SpeechDevice speech, float value) {
        return speech.setBalance(value);
      }
    };

    return control.adjustControl(speech, steps);
  }

  protected boolean adjustRate (SpeechDevice speech, int steps) {
    SpeechControl control = new LogarithmicSpeechControl() {
      @Override
      protected String getLabel () {
        return "rate";
      }

      @Override
      protected float getValue (SpeechDevice speech) {
        return speech.getRate();
      }

      @Override
      protected boolean setValue (SpeechDevice speech, float value) {
        return speech.setRate(value);
      }
    };

    return control.adjustControl(speech, steps);
  }

  protected boolean adjustPitch (SpeechDevice speech, int steps) {
    SpeechControl control = new LogarithmicSpeechControl() {
      @Override
      protected String getLabel () {
        return "pitch";
      }

      @Override
      protected float getValue (SpeechDevice speech) {
        return speech.getPitch();
      }

      @Override
      protected boolean setValue (SpeechDevice speech, float value) {
        return speech.setPitch(value);
      }
    };

    return control.adjustControl(speech, steps);
  }

  protected SpeechAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
