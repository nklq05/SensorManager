package com.ubhave.sensormanager.tasks;

import java.util.ArrayList;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorInterface;

public abstract class AbstractSensorTask extends Thread
{
	protected SensorInterface sensor;
	protected Object syncObject = new Object();
	
	protected int state;
	
	protected ArrayList<SensorDataListener> listenerList;
//	protected ArrayList<SensorConfig> listenerConfigList;

	public static final int RUNNING = 6123;
	public static final int PAUSED = 6124;
	public static final int STOPPED = 6125;
	
	public AbstractSensorTask(SensorInterface sensor)
	{
		this.sensor = sensor;
		listenerList = new ArrayList<SensorDataListener>();
//		listenerConfigList = new ArrayList<SensorConfig>();
	}

	public abstract void run();

//	public boolean isRunning()
//	{
//		if (state == RUNNING)
//		{
//			return true;
//		}
//		return false;
//	}

//	public boolean isPaused()
//	{
//		if (state == PAUSED)
//		{
//			return true;
//		}
//		return false;
//	}

//	public boolean isStopped()
//	{
//		if (state == STOPPED)
//		{
//			return true;
//		}
//		return false;
//	}

	public boolean registerSensorDataListener(SensorDataListener listener)
	{
		synchronized (listenerList)
		{
			for (int i=0; i<listenerList.size(); i++)
			{
				if (listenerList.get(i) == listener)
				{
					return false;
				}
			}
			listenerList.add(listener);
			return true;
		}
	}

	public void unregisterSensorDataListener(SensorDataListener listener)
	{
		synchronized (listenerList)
		{
//			int index = listenerList.indexOf(listener);
			listenerList.remove(listener);
//			listenerConfigList.remove(index);
		}
	}

	public void start()
	{
		state = STOPPED;
		super.start();
	}

	protected SensorConfig getSensorConfig()
	{
		// TODO
//		SensorConfig sensorConfig;
//		if (listenerConfigList.size() > 0)
//		{
//			sensorConfig = listenerConfigList.get(0);
//		}
//		else
//		{
		SensorConfig sensorConfig = AbstractSensor.getDefaultSensorConfig(sensor.getSensorType());
//		}
		return sensorConfig;
	}

	public void startTask() throws ESException
	{
		if (state == STOPPED)
		{
			synchronized (syncObject)
			{
				syncObject.notify();
			}
		}
		else
		{
			throw new ESException(ESException.INVALID_STATE, "cannot start() the sensor task in the pause or running state");
		}
	}

//	protected void pauseTask(long delay) throws ESException
//	{
//		if (state == STOPPED)
//		{
//			throw new ESException(ESException.INVALID_STATE, "cannot pause() the sensor task in the stopped state");
//		}
//		else
//		{
//			pauseTime = delay;
//			synchronized (syncObject)
//			{
//				state = PAUSED;
//				this.interrupt();
//			}
//
//		}
//	}

	protected void stopTask()
	{
		if (state == STOPPED)
		{
			// ignore
		}
		else
		{
			synchronized (syncObject)
			{
				state = STOPPED;
				this.interrupt();
			}
		}
	}

//	protected void logData(SensorData sensorData)
//	{
//		if (sensorData != null)
//		{
//			String sensorName = AbstractSensor.getSensorName(sensor.getSensorType());
//			ESLogger.log("SensorManager", "Data from: " + sensorName);
//			DataLogger.getDataLogger().logData(sensorName, sensorData.toString());
//
//			// update listeners
//			for (SensorDataListener listener : listenerList)
//			{
//				listener.onDataSensed(sensorData);
//			}
//
//			// if the battery level is less than 20% then stop sensing
//			if (sensorData instanceof BatteryData)
//			{
//				BatteryData batteryData = (BatteryData) sensorData;
//				if ((batteryData.getBatteryLevel() < Constants.STOP_SENSING_BATTERY_LEVEL) && (!batteryData.isCharging()))
//				{
////					try
////					{
////						// pause for 30 mins
////						ESSensorManager.this.pauseAllSensors(30 * 60 * 1000);
////					}
////					catch (ESException exp)
////					{
////						ESLogger.error(TAG, exp);
////					}
//				}
//			}
//		}
//	}
}