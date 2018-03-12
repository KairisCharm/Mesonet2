package org.mesonet.app.maps;



public abstract class BaseMapsDataProvider
{
    public abstract void GetMaps(MapsListListener inListListener);



    public interface MapsListListener
    {
        void ListLoaded(MapsModel inMapModel);
    }
}
