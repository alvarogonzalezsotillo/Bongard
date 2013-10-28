package ollitos.bongard.quiz;

import android.app.Activity;
import android.os.Bundle;
import bongard.gui.game.BGameModel;
import bongard.gui.game.BSlidableGameField;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.andr.AndrActivity;

public class BongardQuizActivity  extends AndrActivity {

    @Override
    protected IBDrawable createDefaultDrawable() {
        IBDrawable saved = BPlatform.instance().stateManager().restore(BSlidableGameField.class);
        final IBDrawable d = saved == null ? BGameModel.goToInitialLevel() : saved;
        return d;
    }

}
