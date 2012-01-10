package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;
/**
 * @deprecated use {@link AbstractSendableData} instead
 */
@Deprecated
public class AuthentificationData extends SharedObject implements ISendableData {

	private static final long serialVersionUID = -9150339215596519380L;

	public final String username;
	public final String password;

	public AuthentificationData(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "Authentification{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
