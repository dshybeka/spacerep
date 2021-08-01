# SpaceRep

### Stories

1. I should be able to add new `learning entries`
    - Go to `Create learning entity` page
    - Fill in required fields
    - New `learning entity` should be created and visible at `List learning entities page`
2. Once added `leaning entries` should be scheduled automatically for next repetition
    - `learning entries` should not be scheduled at weekends or holidays
3. I should be able to see `learning entries` scheduled for today
    - Entries should be highlighted(up to 3 entry even if there are more entries scheduled for today)
4. I should be able to see all `learning entries`
6. I should be able to mark `learning entries` as `repeated` or 'failed'.
7. `learning entry` marked as `repeated` should be rescheduled or moved to the archive
8. `learning entry` marked as `failed` should start from first repetition
9. `learning entries` should be archived once marked as completed 10 times
10. I should be able to see list of archived `learning entries`
11. I should be able to edit `learning entries` (notes field)
12. All actions made on `learning entries` should be recordedin `changes` field


### Model
#### LearningEntity
| Field | Type | Notes |
| ------ | ------ | ----- |
| id | number | unique identifier |
| name | String | description of learning entity |
| notes | String | details related to the problem |
| changes | List<String> | list of actions made with the entity: [created, updated, scheduled, change of status] |
| createdAt | Date | creation date |
| updatedAt | Date | last update date |
| attempt | number | number of attempts |
| isArchived | boolean | is record archived |
| archivedAt | Date | arhivation date |
| status | String | current status of entity: [scheduled, repeated, failed, archived] |
| scheduledFor | Date | scheduled for repetition date |
| mark | List<Mark> | mark for the last execution[5 - perfect response,4 - correct response after a hesitation,3 - correct response recalled with serious difficulty,2 - incorrect response; where the correct one seemed easy to recall, 1 - incorrect response; the correct one remembered,0 - complete blackout.] |
| eFactor | List<EasinessFactor> | factor reflecting the easiness of memorizing and retaining a given item in memory |

#### Mark
| Field | Type | Notes |
| ------ | ------ | ----- |
| value | number | actual mark |
| date | Date | date of the mark |

#### EasinessFactor
| Field | Type | Notes |
| ------ | ------ | ----- |
| value | decimal | actual easiness factor |
| date | Date | date of factor calculation |

### Scheduling
Scheduling should be implemented with support for multiple ways to schedule next repetition.

#### Default ([more details](https://www.supermemo.com/en/archives1990-2015/english/ol/sm2))
1. Set default eFactor = 2.5
2. first repetition: 7 days, for n>2: I(n):=I(n-1)*eFactor
3. update eFactor with new value: EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02)) but no lower than 1.3
4. archive entry if last 10 attempts for entry have 4+ mark





### Reference Documentation
##### Deployment
gcloud app deploy