import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderMaker {
	
    static JSlider getSlider(final JOptionPane optionPane) {
        JSlider GUISlider = new JSlider(3, 80, 10);

        ChangeListener listener = new ChangeListener() {
          public void stateChanged(ChangeEvent changeEvent) {
            JSlider theSlider = (JSlider) changeEvent.getSource();
            if (!theSlider.getValueIsAdjusting()) {
              optionPane.setInputValue(theSlider.getValue());
            }
          }
        };
        GUISlider.addChangeListener(listener);
        return GUISlider;
      }

}
