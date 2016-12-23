/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * Toast.java at 2016-2-20 11:23:00, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package org.jb2011.swing9patch.toast;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.Timer;

import org.jb2011.lnf.beautyeye.utils.WindowTranslucencyHelper;

public class Toast extends JDialog implements ActionListener
{
	private Point showPossition = null;
	private Timer timer = null;
	private ToastPane toastPane = null; 
	
	public Toast(int delay, String message, Point p)
	{
//		super(parent);
		initGUI();
		
		// init datas
		timer = new Timer(delay, this);
		toastPane.setMessage(message);
		this.showPossition = p;
	}
	
	protected void initGUI()
	{
		// set dialog full transparent
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
//		AWTUtilities.setWindowOpaque(this, false);
		WindowTranslucencyHelper.setWindowOpaque(this, false);
//		this.setBackground(new Color(0,0,0,0));
		// contentPane default is opaque in Java1.7+
		((JComponent)(this.getContentPane())).setOpaque(false);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		// init main layout
		toastPane = new ToastPane();
		this.add(toastPane);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// fade out
		for(float i=1.0f; i>=0;i-=0.05f)
		try{
//			AWTUtilities.setWindowOpacity(this, i);
			WindowTranslucencyHelper.setOpacity(this, i);
			Thread.sleep(50);
		}
		catch (Exception e2){
		}
		
		// dispose it
		if(timer != null)
			timer.stop();
		this.dispose();
	}
	
	public Toast showItNow()
	{
		this.pack();
		if(showPossition == null || (showPossition.x<0&&showPossition.y<0))
			this.setLocationRelativeTo(null);
		else
			this.setLocation(new Point(showPossition.x<0?0:showPossition.x, showPossition.y<0?0:showPossition.y));
		this.setVisible(true);
		timer.start();
		return this;
	}
	
	public static Toast showTost(int delay, String message, Point p)
	{
		return new Toast(delay, message, p).showItNow();
	}
}
