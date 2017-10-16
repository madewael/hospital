package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.units.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room,String> {

}