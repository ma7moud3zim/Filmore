import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing-module';
import { ManageVideo } from './dialog/manage-video/manage-video';
import { VideoList } from './video-list/video-list';
import { SharedModule } from '../shared/shared-module';
import { A11yModule } from '@angular/cdk/a11y';

@NgModule({
  declarations: [ManageVideo, VideoList],
  imports: [CommonModule, AdminRoutingModule, SharedModule, A11yModule],
})
export class AdminModule {}
