import { Injectable } from '@angular/core';
import { KeycloakProfile } from 'keycloak-js';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  userProfile: KeycloakProfile | null = null

  constructor(private keycloak: Keycloak) { }

  async getUserProfile(): Promise<KeycloakProfile | null> {
    if (!this.userProfile) {
      this.userProfile = await this.keycloak.loadUserProfile();
    }
    return this.userProfile
  }
  
  async getProfileLink(){
    return this.keycloak.accountManagement();
  }
  
  async logout() {
    await this.keycloak.logout();
  }
}
