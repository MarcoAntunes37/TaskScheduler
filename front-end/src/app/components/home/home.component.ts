import { Component, Input } from '@angular/core';
import { UserProfileService } from '../../services/user-profile/user-profile.service';
import { KeycloakProfile } from 'keycloak-js';
import { UppercaseFirstLetterPipe } from '../../pipelines/uppercase-first-letter.pipe';

@Component({
  selector: 'app-home',
  imports: [UppercaseFirstLetterPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  userProfile: KeycloakProfile | null = null;
  constructor(
    private readonly UserProfileService: UserProfileService
  ) { }

  async ngOnInit() {
    this.userProfile = await this.UserProfileService.getUserProfile();
  }
}
