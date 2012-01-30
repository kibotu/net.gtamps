package net.gtamps.shared.serializer.communication;

public class SendableProviderSingleton {
	private static SendableProvider sendableProvider = null;

	public static SendableProvider getInstance(){
		if(sendableProvider == null){
			sendableProvider = new SendableProvider(new SendableCacheFactory());
		}
		return sendableProvider;
	}
}
