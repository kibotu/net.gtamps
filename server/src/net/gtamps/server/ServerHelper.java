package net.gtamps.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

public class ServerHelper {
    /**
     * Returns all available IP addresses.
     * <p>
     * To get the first/main local ip only you could use also
     * {@link #getLocalIP() }.
     * <p>
     * In error case or if no network connection is established, we return
     * an empty list here.
     * <p>
     * Loopback addresses are excluded - so 127.0.0.1 will not be never
     * returned.
     * <p>
     * The "primary" IP might not be the first one in the returned list.
     *
     * @return  Returns all IP addresses (can be an empty list in error case
     *          or if network connection is missing).
     * @since   0.1.0
     * 
     * http://it-tactics.blogspot.com/2010/07/get-reliable-local-ip-address-in-java.html
     * 7.12.2011

     * 
     */
    public static Collection<InetAddress> getAllLocalIPs()
    {
        final LinkedList<InetAddress> listAdr = new LinkedList<InetAddress>();
        try
        {
            final Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            if (nifs == null) {
				return listAdr;
			}

            while (nifs.hasMoreElements())
            {
                final NetworkInterface nif = nifs.nextElement();
                // We ignore subinterfaces - as not yet needed.

                final Enumeration<InetAddress> adrs = nif.getInetAddresses();
                while (adrs.hasMoreElements())
                {
                    final InetAddress adr = adrs.nextElement();
                    if (adr != null && !adr.isLoopbackAddress() && (nif.isPointToPoint() || !adr.isLinkLocalAddress()))
                    {
                        listAdr.add(adr);
                    }
                }
            }
            return listAdr;
        }
        catch (final SocketException ex)
        {
//            Logger.getLogger(Net.class.getName()).log(Level.WARNING, "No IP address available", ex);
            return listAdr;
        }
    }
	
    /**
     * Returns the current local IP address or an empty string in error case /
     * when no network connection is up.
     * <p>
     * The current machine could have more than one local IP address so might
     * prefer to use {@link #getAllLocalIPs() } or
     * {@link #getAllLocalIPs(java.lang.String) }.
     * <p>
     * If you want just one IP, this is the right method and it tries to find
     * out the most accurate (primary) IP address. It prefers addresses that
     * have a meaningful dns name set for example.
     *
     *
     *
     * @return  Returns the current local IP address or an empty string in error case.
     * @since   0.1.0
     * 
     * http://it-tactics.blogspot.com/2010/07/get-reliable-local-ip-address-in-java.html
     * 7.12.2011
     * 
     */
    public static String getLocalIP()
    {

        //// This method does not work any more - I think on Windows it worked by accident
        //try
        //{
        //    String ip = InetAddress.getLocalHost().getHostAddress();
        //    return ip;
        //}
        //catch (UnknownHostException ex)
        //{
        //    Logger.getLogger(Net.class.getName()).log(Level.WARNING, null, ex);
        //    return "";
        //}
        String ipOnly = "";
        try
        {
            final Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            if (nifs == null) {
				return "";
			}
            while (nifs.hasMoreElements())
            {
                final NetworkInterface nif = nifs.nextElement();
                // We ignore subinterfaces - as not yet needed.

                if (!nif.isLoopback() && nif.isUp() && !nif.isVirtual())
                {
                    final Enumeration<InetAddress> adrs = nif.getInetAddresses();
                    while (adrs.hasMoreElements())
                    {
                        final InetAddress adr = adrs.nextElement();
                        if (adr != null && !adr.isLoopbackAddress() && (nif.isPointToPoint() || !adr.isLinkLocalAddress()))
                        {
                            final String adrIP = adr.getHostAddress();
                            String adrName;
                            if (nif.isPointToPoint()) {
								adrName = adrIP;
							} else {
								adrName = adr.getCanonicalHostName();
							}

                            if (!adrName.equals(adrIP)) {
								return adrIP;
							} else {
								ipOnly = adrIP;
							}
                        }
                    }
                }
            }
//            if (ipOnly.length()==0) Logger.getLogger(Net.class.getName()).log(Level.WARNING, "No IP address available");
            return ipOnly;
        }
        catch (final SocketException ex)
        {
//            Logger.getLogger(Net.class.getName()).log(Level.WARNING, "No IP address available", ex);
            return "";
        }
    }
    
    /**
     * 
     * @param ifname
     * @return
     * 
     * based on code from
     * http://stackoverflow.com/questions/1062041/ip-address-not-obtained-in-java
     * 7.12.2011
     * 
     */
    public static InetAddress getPrimaryAddressForInterface(final String ifname) {
		InetAddress inadr = null;
		try {
			for (final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				final NetworkInterface iface = ifaces.nextElement();
				if (ifname.equals(iface.getName())) {
					for (final Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();) {
						final InetAddress adr = addresses.nextElement();
                        if (adr != null && !adr.isLoopbackAddress() && (iface.isPointToPoint() || !adr.isLinkLocalAddress())) {
                        	inadr =addresses.nextElement();
                        }
					}
				}
			}
		} catch (final Exception e) {
			System.err.println("Error getting inet address for interface " + ifname);
			e.printStackTrace();
		}
		return inadr;
	}
}
