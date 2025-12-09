package org.example.project_wobimich.mapper;

import org.example.project_wobimich.UserLocation;
import org.example.project_wobimich.dto.AddressDTO;

public class UserLocationMapper {

    public static UserLocation mapToUserLocation(AddressDTO addressDTO) {
        UserLocation location = null;
        if (addressDTO != null) {
            location = new UserLocation(
                    addressDTO.getStreetName(), addressDTO.getStreetNumber(), addressDTO.getPostalCode(),
                    addressDTO.getCity(),addressDTO.getLongitude(),addressDTO.getLatitude());
        }
        return location;
    }

}
