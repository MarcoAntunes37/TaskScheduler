import { Routes } from '@angular/router';
import { TasksComponent } from './components/tasks/tasks.component';
import { HomeComponent } from './components/home/home.component';
import { ScheduleComponent } from './components/schedule/schedule.component';
import { canActivateAuthRole } from './auth.guard';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { NotfoundComponent } from './notfound/notfound.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent, 
        canActivate: [canActivateAuthRole], data: { roles: 'view-home' } 
    },
    { path: 'tasks', component: TasksComponent, 
        canActivate: [canActivateAuthRole], data: { roles: 'view-tasks' } 
    },
    { path: 'scheduler', component: ScheduleComponent, 
        canActivate: [canActivateAuthRole], data: { roles: 'view-scheduler' } 
    },
    { path: 'forbidden', component: ForbiddenComponent },
    { path: '**', component: NotfoundComponent }
];
