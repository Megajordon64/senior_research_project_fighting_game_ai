import java.util.Random;

import aiinterface.AIInterface;
import struct.FrameData;
import struct.GameData;
import struct.Key;

// random based AI code intended for testing alphazero based AI, will likely be considered the easy level AI
public class RandomAI implements AIInterface {

	// Key class for return.
	Key inputKey;
	// is used for getting a random number.
	Random rnd;

	boolean playerNumber;

	@Override
	public void close() {

	}

	@Override
	public void getInformation(FrameData frameData) {

	}

	@Override
	public int initialize(GameData gameData,boolean playerNumber) {
		// initializes a Key instance.
		inputKey = new Key();
		// initializes a random instance.
		rnd = new Random();

		return 0;
	}

	@Override
	public Key input() {
		// returns Key
		return inputKey;
	}

	@Override
	public void processing() {
		// every key is set randomly.
		inputKey.A = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.B = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.C = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.U = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.D = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.L = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.R = (rnd.nextInt(10) > 4) ? true : false;
	}

	@Override
	public void roundEnd(int p1Hp, int p2Hp, int frames){

	}

}
