import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterOutlet } from '@angular/router';
import { KeycloakProfile } from 'keycloak-js';
import { UserProfileService } from './services/user-profile/user-profile.service';
import { UppercaseFirstLetterPipe } from './pipelines/uppercase-first-letter.pipe';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrl: 'app.component.scss',
  imports: [MatToolbarModule, MatSidenavModule, MatButtonModule, RouterOutlet, UppercaseFirstLetterPipe],
})
export class AppComponent implements OnInit {
  title = 'front-end';
  userProfile: KeycloakProfile | null = null
  constructor(private userProfileService: UserProfileService) { }

  async ngOnInit() {
    this.userProfile = await this.userProfileService.getUserProfile();
  }

  async getProfileLink() {
    return this.userProfileService.getProfileLink();
  }

  logout() {
    this.userProfileService.logout();
  }
}
