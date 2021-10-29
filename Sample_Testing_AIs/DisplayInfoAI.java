import aiinterface.CommandCenter;
import aiinterface.AIInterface;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.ScreenData;

/**
 * The AI which decides its next action using display pixel data
 *
 * @author Makoto Ishihara
 */

// this AI will likely be used as the medium level AI for training the alphazero AI, many thanks to Makoto Ishihara
public class DisplayInfoAI implements AIInterface {

	private Key key;
	private CommandCenter commandCenter;
	private boolean playerNumber;
	private GameData gameData;
	private FrameData frameData;
	private ScreenData screenData;
	
	/** The width of the display to obtain */
	private final int WIDTH = 96;

	/** The height of the display to obtain */
	private final int HEIGHT = 64;

	/**
	 * The display's color to obtain true → gray scale, false → RGB
	 */
	private final boolean GRAY_SCALE = true;

	@Override
	public int initialize(GameData gd, boolean p) {

		playerNumber = p;
		gameData = gd;

		key = new Key();
		frameData = new FrameData();
		commandCenter = new CommandCenter();

		return 0;
	}

	@Override
	public void getInformation(FrameData fd) {

		frameData = fd;
		commandCenter.setFrameData(frameData, playerNumber);
	}

	@Override
	public void getScreenData(ScreenData sd) {
		screenData = sd;
	}
	
	@Override
	public void processing() {

		if (canProcessing()) {
			if (commandCenter.getSkillFlag()) {
				key = commandCenter.getSkillKey();
			} else {
				key.empty();
				commandCenter.skillCancel();

				// get display pixel data
				byte[] displayInfo = screenData.getDisplayByteBufferAsBytes(WIDTH, HEIGHT, GRAY_SCALE);
				int leftX = -1;
				int rightX = -1;
				byte prePixel = 0;

				for (int i = 0; i < WIDTH * HEIGHT; i++) {

					// When searching for the same row is over, reset each data
					if (i >= WIDTH && i % WIDTH == 0) {
						prePixel = 0;
						leftX = -1;
						rightX = -1;
					}

					// record x coordinate of the character on right side
					if (displayInfo[i] != 0 && prePixel == 0 && leftX != -1) {
						rightX = i - 1;
						break;
					}

					// record x coordinate of the character on left side
					if (prePixel != 0 && displayInfo[i] == 0) {
						leftX = i - 1;
					}

					// update pixel data
					prePixel = displayInfo[i];
				}

				// default action
				if (leftX == -1 || rightX == -1) {
					commandCenter.commandCall("STAND_A");
				} else {
					int distance = Math.abs(leftX - rightX);
					double close = 80 * ((double) WIDTH / gameData.getStageWidth());
					double far = 200 * ((double) WIDTH / gameData.getStageWidth());

					// conduct action according to the distance based on pixel
					// data
					if (distance < close) {
						commandCenter.commandCall("CROUCH_B");
					} else if (distance >= close && distance < far) {
						commandCenter.commandCall("STAND_FB");
					} else {
						commandCenter.commandCall("STAND_D_DF_FA");
					}
				}
			}
		}
	}

	@Override
	public void close() {
	}

	@Override
	public Key input() {
		return key;
	}

	@Override
	public void roundEnd(int arg0, int arg1, int arg2) {

	}

	private boolean canProcessing() {
		return !frameData.getEmptyFlag() && frameData.getRemainingFramesNumber() > 0;
	}
}
