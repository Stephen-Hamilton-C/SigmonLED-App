#include "paletteform.h"
#include "devicemanager.h"

PaletteForm::PaletteForm(QObject *parent) {}

void PaletteForm::ApplyChanges()
{
	qDebug() << "Apply";
	DeviceManager* dm = DeviceManager::getInstance();

	//Set the brightness
	dm->QueueWrite("B"+brightness);

	//Let's walk through this mess.
	//First line is solid palette (P) or standard running palette (p).
	//Next line is the palette selected, converted into the Arduino char.
	//Last line is the linear blending toggle. l for blending, n for none.
	dm->QueueWrite((solidPalette ? "P" : "p")
				   + palette +
				   (linearBlending ? "l" : "n"));

	//Set time between color updates
	dm->QueueWrite("d"+delay);
}

void PaletteForm::setPalette(QString palette)
{
	qDebug() << "Palette:" << palette;
	this->palette = sigmonPalette[palette];
}

void PaletteForm::setLinearBlending(bool linearBlending)
{
	qDebug() << "Blending:" << linearBlending;
	this->linearBlending = linearBlending;
}

void PaletteForm::setSolidPalette(bool solidPalette)
{
	qDebug() << "Solid Palette:" << solidPalette;
	this->solidPalette = solidPalette;
}

void PaletteForm::setDelay(int delay)
{
	qDebug() << "Delay:" << delay;
	this->delay = DeviceManager::getInstance()->ConvertNumToWritable(delay, true);
}

void PaletteForm::setBrightness(int brightness)
{
	qDebug() << "Brightness:" << brightness;
	this->brightness = DeviceManager::getInstance()->ConvertNumToWritable(brightness);
}
