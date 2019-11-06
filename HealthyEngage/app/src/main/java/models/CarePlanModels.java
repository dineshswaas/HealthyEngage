package models;


import java.io.Serializable;
import java.util.List;

public class CarePlanModels implements Serializable{

    String lastSyncDate;
    boolean isSelected,indicator;
    String monthString;
    String yearString;
    String dayString;
    int dayInt,statusCode;
    String dates;
    boolean today;
    private String TP_Full_Day;
    int position;
    String carePlanId,day,patientId,name,message;

    String id,plan_no,title,goal,problem,plan_start_date,plan_end_date,current_cycle_start_date,current_cycle_end_date;
    String cycle_status,careplan_status,created_at,updated_at,careplan_template_id,owner_id,category_id,organisation_id;
    boolean is_active;

    int plan_duration,cycles,break_time,current_cycle;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    List<CarePlanAssessment> careplanAssessment;
    List<CarePlanInstruction> careplanInstruction;
    List<CarePlanIntervention> careplanIntervention;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarePlanId() {
        return carePlanId;
    }

    public void setCarePlanId(String carePlanId) {
        this.carePlanId = carePlanId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTP_Full_Day() {
        return TP_Full_Day;
    }

    public void setTP_Full_Day(String TP_Full_Day) {
        this.TP_Full_Day = TP_Full_Day;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getDayInt() {
        return dayInt;
    }

    public void setDayInt(int day) {
        this.dayInt = day;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public String getYearString() {
        return yearString;
    }

    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }



        public List<CarePlanIntervention> getCareplanIntervention() {
            return careplanIntervention;
        }

        public void setCareplanIntervention(List<CarePlanIntervention> careplanIntervention) {
            this.careplanIntervention = careplanIntervention;
        }

        public List<CarePlanInstruction> getCareplanInstruction() {
            return careplanInstruction;
        }

        public void setCareplanInstruction(List<CarePlanInstruction> careplanInstruction) {
            this.careplanInstruction = careplanInstruction;
        }

        public List<CarePlanAssessment> getCareplanAssessment() {
            return careplanAssessment;
        }

        public void setCareplanAssessment(List<CarePlanAssessment> careplanAssessment) {
            this.careplanAssessment = careplanAssessment;
        }

        public String getPlan_no() {
            return plan_no;
        }

        public void setPlan_no(String plan_no) {
            this.plan_no = plan_no;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }

        public String getPlan_start_date() {
            return plan_start_date;
        }

        public void setPlan_start_date(String plan_start_date) {
            this.plan_start_date = plan_start_date;
        }

        public String getPlan_end_date() {
            return plan_end_date;
        }

        public void setPlan_end_date(String plan_end_date) {
            this.plan_end_date = plan_end_date;
        }

        public String getCurrent_cycle_start_date() {
            return current_cycle_start_date;
        }

        public void setCurrent_cycle_start_date(String current_cycle_start_date) {
            this.current_cycle_start_date = current_cycle_start_date;
        }

        public String getCurrent_cycle_end_date() {
            return current_cycle_end_date;
        }

        public void setCurrent_cycle_end_date(String current_cycle_end_date) {
            this.current_cycle_end_date = current_cycle_end_date;
        }

        public String getCycle_status() {
            return cycle_status;
        }

        public void setCycle_status(String cycle_status) {
            this.cycle_status = cycle_status;
        }

        public String getCareplan_status() {
            return careplan_status;
        }

        public void setCareplan_status(String careplan_status) {
            this.careplan_status = careplan_status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCareplan_template_id() {
            return careplan_template_id;
        }

        public void setCareplan_template_id(String careplan_template_id) {
            this.careplan_template_id = careplan_template_id;
        }

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getOrganisation_id() {
            return organisation_id;
        }

        public void setOrganisation_id(String organisation_id) {
            this.organisation_id = organisation_id;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }

        public int getPlan_duration() {
            return plan_duration;
        }

        public void setPlan_duration(int plan_duration) {
            this.plan_duration = plan_duration;
        }

        public int getCycles() {
            return cycles;
        }

        public void setCycles(int cycles) {
            this.cycles = cycles;
        }

        public int getBreak_time() {
            return break_time;
        }

        public void setBreak_time(int break_time) {
            this.break_time = break_time;
        }

        public int getCurrent_cycle() {
            return current_cycle;
        }

        public void setCurrent_cycle(int current_cycle) {
            this.current_cycle = current_cycle;
        }



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }




        public class CarePlanAssessment implements Serializable {

            String id,name,target_or_time,positve_label,negative_label,description,start_time,end_time,input_type,question,min,max,threshold,label_min,label_max,created_at,updated_at,is_deleted,careplan_id,organisation_id;
            int prompt_time;

            public String getPositve_label() {
                return positve_label;
            }

            public void setPositve_label(String positve_label) {
                this.positve_label = positve_label;
            }

            public String getNegative_label() {
                return negative_label;
            }

            public void setNegative_label(String negative_label) {
                this.negative_label = negative_label;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            List<PatientAssessment> patientAssessment;

            public List<PatientAssessment> getPatientAssessment() {
                return patientAssessment;
            }

            public void setPatientAssessment(List<PatientAssessment> patientAssessment) {
                this.patientAssessment = patientAssessment;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTarget_or_time() {
                return target_or_time;
            }

            public void setTarget_or_time(String target_or_time) {
                this.target_or_time = target_or_time;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getInput_type() {
                return input_type;
            }

            public void setInput_type(String input_type) {
                this.input_type = input_type;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }

            public String getThreshold() {
                return threshold;
            }

            public void setThreshold(String threshold) {
                this.threshold = threshold;
            }

            public String getLabel_min() {
                return label_min;
            }

            public void setLabel_min(String label_min) {
                this.label_min = label_min;
            }

            public String getLabel_max() {
                return label_max;
            }

            public void setLabel_max(String label_max) {
                this.label_max = label_max;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getIs_deleted() {
                return is_deleted;
            }

            public void setIs_deleted(String is_deleted) {
                this.is_deleted = is_deleted;
            }

            public String getCareplan_id() {
                return careplan_id;
            }

            public void setCareplan_id(String careplan_id) {
                this.careplan_id = careplan_id;
            }

            public String getOrganisation_id() {
                return organisation_id;
            }

            public void setOrganisation_id(String organisation_id) {
                this.organisation_id = organisation_id;
            }

            public int getPrompt_time() {
                return prompt_time;
            }

            public void setPrompt_time(int prompt_time) {
                this.prompt_time = prompt_time;
            }


            public class PatientAssessment implements Serializable{

                String id,value,created_at,updated_at,assessment_date,careplan_id,organisation_id,delegate_id,careplan_assessment_id,patient_id;
                boolean is_latest;
                int day;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getAssessment_date() {
                    return assessment_date;
                }

                public void setAssessment_date(String assessment_date) {
                    this.assessment_date = assessment_date;
                }

                public String getCareplan_id() {
                    return careplan_id;
                }

                public void setCareplan_id(String careplan_id) {
                    this.careplan_id = careplan_id;
                }

                public String getOrganisation_id() {
                    return organisation_id;
                }

                public void setOrganisation_id(String organisation_id) {
                    this.organisation_id = organisation_id;
                }

                public String getDelegate_id() {
                    return delegate_id;
                }

                public void setDelegate_id(String delegate_id) {
                    this.delegate_id = delegate_id;
                }

                public String getCareplan_assessment_id() {
                    return careplan_assessment_id;
                }

                public void setCareplan_assessment_id(String careplan_assessment_id) {
                    this.careplan_assessment_id = careplan_assessment_id;
                }

                public String getPatient_id() {
                    return patient_id;
                }

                public void setPatient_id(String patient_id) {
                    this.patient_id = patient_id;
                }

                public boolean isIs_latest() {
                    return is_latest;
                }

                public void setIs_latest(boolean is_latest) {
                    this.is_latest = is_latest;
                }

                public int isDay() {
                    return day;
                }

                public void setDay(int day) {
                    this.day = day;
                }
            }
        }


        public class CarePlanInstruction implements Serializable{
            String id,title,label,instructions,created_at,updated_at,careplan_id,organisation_id;
            boolean is_deleted;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getInstructions() {
                return instructions;
            }

            public void setInstructions(String instructions) {
                this.instructions = instructions;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getCareplan_id() {
                return careplan_id;
            }

            public void setCareplan_id(String careplan_id) {
                this.careplan_id = careplan_id;
            }

            public String getOrganisation_id() {
                return organisation_id;
            }

            public void setOrganisation_id(String organisation_id) {
                this.organisation_id = organisation_id;
            }

            public boolean isIs_deleted() {
                return is_deleted;
            }

            public void setIs_deleted(boolean is_deleted) {
                this.is_deleted = is_deleted;
            }
        }


        public class CarePlanIntervention implements Serializable{


            List<InterventionDay> interventionDay;
            List<InterventionFrequency> interventionFrequency;

            public List<InterventionFrequency> getInterventionFrequency() {
                return interventionFrequency;
            }

            List<InterventionElements> interventionElements;

            public List<InterventionElements> getInterventionElements() {
                return interventionElements;
            }

            public void setInterventionElements(List<InterventionElements> interventionElements) {
                this.interventionElements = interventionElements;
            }

            public List<InterventionDay> getInterventionDay() {
                return interventionDay;
            }

            public void setInterventionDay(List<InterventionDay> interventionDay) {
                this.interventionDay = interventionDay;
            }

            public void setInterventionFrequency(List<InterventionFrequency> interventionFrequency) {
                this.interventionFrequency = interventionFrequency;
            }









            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getDosage() {
                return dosage;
            }

            public void setDosage(String dosage) {
                this.dosage = dosage;
            }

            public String getInstructions() {
                return instructions;
            }

            public void setInstructions(String instructions) {
                this.instructions = instructions;
            }

            public String getTotal_days() {
                return total_days;
            }

            public void setTotal_days(String total_days) {
                this.total_days = total_days;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getCareplan_id() {
                return careplan_id;
            }

            public void setCareplan_id(String careplan_id) {
                this.careplan_id = careplan_id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getOrganisation_id() {
                return organisation_id;
            }

            public void setOrganisation_id(String organisation_id) {
                this.organisation_id = organisation_id;
            }

            public int getFrequency() {
                return frequency;
            }

            public void setFrequency(int frequency) {
                this.frequency = frequency;
            }

            public int getSkip_days() {
                return skip_days;
            }

            public void setSkip_days(int skip_days) {
                this.skip_days = skip_days;
            }

            public boolean isIs_deleted() {
                return is_deleted;
            }

            public void setIs_deleted(boolean is_deleted) {
                this.is_deleted = is_deleted;
            }

            public int[] getDays() {
                return days;
            }

            public void setDays(int[] days) {
                this.days = days;
            }

            public String[] getReminders() {
                return reminders;
            }

            public void setReminders(String[] reminders) {
                this.reminders = reminders;
            }

            String id,name,color,dosage,instructions,total_days,created_at,updated_at,careplan_id,category_id,organisation_id;
            int frequency,skip_days;
            boolean is_deleted;
            int days[];
            String reminders[];


            public  class InterventionFrequency{
                String id,reminder,created_at,updated_at,careplan_intervention_id,organisation_id;
                boolean is_completed;

                public boolean isIs_completed() {
                    return is_completed;
                }

                public void setIs_completed(boolean is_completed) {
                    this.is_completed = is_completed;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getReminder() {
                    return reminder;
                }

                public void setReminder(String reminder) {
                    this.reminder = reminder;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getCareplan_intervention_id() {
                    return careplan_intervention_id;
                }

                public void setCareplan_intervention_id(String careplan_intervention_id) {
                    this.careplan_intervention_id = careplan_intervention_id;
                }

                public String getOrganisation_id() {
                    return organisation_id;
                }

                public void setOrganisation_id(String organisation_id) {
                    this.organisation_id = organisation_id;
                }
            }

            public class InterventionDay implements Serializable{

                String id,day,created_at,updated_at,careplan_intervention_id,organisation_id,patient_id;
                boolean is_completed,is_active;
                int cycle_no;

                List<PatientIntervention> patientIntervention;
                List<DeletedPatientIntervention>deletedPatientIntervention;

                public List<DeletedPatientIntervention> getDeletedPatientIntervention() {
                    return deletedPatientIntervention;
                }

                public void setDeletedPatientIntervention(List<DeletedPatientIntervention> deletedPatientIntervention) {
                    this.deletedPatientIntervention = deletedPatientIntervention;
                }

                public List<PatientIntervention> getPatientIntervention() {
                    return patientIntervention;
                }

                public void setPatientIntervention(List<PatientIntervention> patientIntervention) {
                    this.patientIntervention = patientIntervention;
                }


                public class DeletedPatientIntervention{
                    String id,delegate_id,intervention_date,created_at,intervention_days_id,intervention_frequency_id,organisation_id,patient_id;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getDelegate_id() {
                        return delegate_id;
                    }

                    public void setDelegate_id(String delegate_id) {
                        this.delegate_id = delegate_id;
                    }

                    public String getIntervention_date() {
                        return intervention_date;
                    }

                    public void setIntervention_date(String intervention_date) {
                        this.intervention_date = intervention_date;
                    }

                    public String getCreated_at() {
                        return created_at;
                    }

                    public void setCreated_at(String created_at) {
                        this.created_at = created_at;
                    }

                    public String getIntervention_days_id() {
                        return intervention_days_id;
                    }

                    public void setIntervention_days_id(String intervention_days_id) {
                        this.intervention_days_id = intervention_days_id;
                    }

                    public String getIntervention_frequency_id() {
                        return intervention_frequency_id;
                    }

                    public void setIntervention_frequency_id(String intervention_frequency_id) {
                        this.intervention_frequency_id = intervention_frequency_id;
                    }

                    public String getOrganisation_id() {
                        return organisation_id;
                    }

                    public void setOrganisation_id(String organisation_id) {
                        this.organisation_id = organisation_id;
                    }

                    public String getPatient_id() {
                        return patient_id;
                    }

                    public void setPatient_id(String patient_id) {
                        this.patient_id = patient_id;
                    }
                }


                public class PatientIntervention{
                    String id,delegate_id,intervention_date,created_at,intervention_days_id,intervention_frequency_id,organisation_id,patient_id;

                   public String getId() {
                       return id;
                   }

                   public void setId(String id) {
                       this.id = id;
                   }

                   public String getDelegate_id() {
                       return delegate_id;
                   }

                   public void setDelegate_id(String delegate_id) {
                       this.delegate_id = delegate_id;
                   }

                   public String getIntervention_date() {
                       return intervention_date;
                   }

                   public void setIntervention_date(String intervention_date) {
                       this.intervention_date = intervention_date;
                   }

                   public String getCreated_at() {
                       return created_at;
                   }

                   public void setCreated_at(String created_at) {
                       this.created_at = created_at;
                   }

                   public String getIntervention_days_id() {
                       return intervention_days_id;
                   }

                   public void setIntervention_days_id(String intervention_days_id) {
                       this.intervention_days_id = intervention_days_id;
                   }

                   public String getIntervention_frequency_id() {
                       return intervention_frequency_id;
                   }

                   public void setIntervention_frequency_id(String intervention_frequency_id) {
                       this.intervention_frequency_id = intervention_frequency_id;
                   }

                   public String getOrganisation_id() {
                       return organisation_id;
                   }

                   public void setOrganisation_id(String organisation_id) {
                       this.organisation_id = organisation_id;
                   }

                   public String getPatient_id() {
                       return patient_id;
                   }

                   public void setPatient_id(String patient_id) {
                       this.patient_id = patient_id;
                   }
               }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getCareplan_intervention_id() {
                    return careplan_intervention_id;
                }

                public void setCareplan_intervention_id(String careplan_intervention_id) {
                    this.careplan_intervention_id = careplan_intervention_id;
                }

                public String getOrganisation_id() {
                    return organisation_id;
                }

                public void setOrganisation_id(String organisation_id) {
                    this.organisation_id = organisation_id;
                }

                public String getPatient_id() {
                    return patient_id;
                }

                public void setPatient_id(String patient_id) {
                    this.patient_id = patient_id;
                }

                public boolean isIs_completed() {
                    return is_completed;
                }

                public void setIs_completed(boolean is_completed) {
                    this.is_completed = is_completed;
                }

                public boolean isIs_active() {
                    return is_active;
                }

                public void setIs_active(boolean is_active) {
                    this.is_active = is_active;
                }

                public int getCycle_no() {
                    return cycle_no;
                }

                public void setCycle_no(int cycle_no) {
                    this.cycle_no = cycle_no;
                }
            }

        }



}
