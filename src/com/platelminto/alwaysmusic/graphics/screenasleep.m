#import <Foundation/Foundation.h>

boolean_t is_asleep() {
    
    const int display_limit = 16;
    boolean_t asleep = true;
    CGDirectDisplayID ids[display_limit];
    uint32_t num_ids;
    
    CGGetActiveDisplayList(display_limit, ids, &num_ids);
    
    for (int i = 0; i < num_ids; i++) {
        
        asleep &= CGDisplayIsAsleep(ids[i]);
    }
    
    return asleep;
}

int main(int argc, char *argv[]) {
    
    const boolean_t asleep = is_asleep();
    
    printf("%s\n", asleep ? "true" : "false");
    return asleep;
}